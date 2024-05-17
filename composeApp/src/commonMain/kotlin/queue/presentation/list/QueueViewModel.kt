package queue.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmk.kmpnotifier.notification.NotifierManager
import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.asString
import core.domain.utils.onFailure
import core.domain.utils.onSuccess
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission.REMOTE_NOTIFICATION
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.notification_permission_error
import onthewakelive.composeapp.generated.resources.reconnect
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import queue.domain.model.Line
import queue.domain.model.ReorderedQueueItem
import queue.domain.repository.QueueRepository
import queue.presentation.list.QueueEvent.ConnectToSession
import queue.presentation.list.QueueEvent.LeaveQueueConfirmationDialogDismissRequest
import queue.presentation.list.QueueEvent.OnQueueItemClicked
import queue.presentation.list.QueueEvent.OnQueueItemDetailsDialogDismissRequest
import queue.presentation.list.QueueEvent.OnQueueReordered
import queue.presentation.list.QueueEvent.OnSaveReorderedQueueClicked
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToQueueAdminScreen
import queue.presentation.list.QueueViewModel.QueueAction.ShowError
import queue.presentation.list.QueueViewModel.QueueAction.ShowPermissionError
import user_profile.domain.use_case.GetQueueItemDetailsUseCase
import user_profile.domain.use_case.IsUserSubscribedUseCase

@OptIn(ExperimentalResourceApi::class)
class QueueViewModel(
    private val queueRepository: QueueRepository,
    private val getQueueItemDetailsUseCase: GetQueueItemDetailsUseCase,
    private val isUserSubscribed: IsUserSubscribedUseCase,
    val permissionsController: PermissionsController
) : ViewModel() {

    private val _state = MutableStateFlow(QueueState())
    val state: StateFlow<QueueState> = _state.asStateFlow()

    private val _action = Channel<QueueAction>()
    val actions: Flow<QueueAction> = _action.receiveAsFlow()

    private var queueObserverJob: Job? = null

    init {
        viewModelScope.launch {
            queueRepository.connectionStatus.collect { isConnected ->
                _state.update { it.copy(isConnected = isConnected) }
            }
        }
    }

    fun onEvent(event: QueueEvent) {
        when (event) {
            ConnectToSession -> if (!queueRepository.isSessionActive) {
                initSession()
            } else {
                getQueue()
            }

            is QueueEvent.OnJoinClicked -> {
                if (event.isUserAdmin) {
                    viewModelScope.launch {
                        _action.send(NavigateToQueueAdminScreen(line = event.line))
                    }
                } else {
                    joinTheQueue(userId = event.userId, line = event.line)
                }
            }

            is QueueEvent.OnQueueLeaved -> {
                toggleLeaveQueueConfirmationDialog()
                _state.update { it.copy(queueItemIdToDelete = event.queueItemId) }
            }

            LeaveQueueConfirmationDialogDismissRequest -> toggleLeaveQueueConfirmationDialog()

            is QueueEvent.OnLeaveQueueConfirmed -> {
                toggleLeaveQueueConfirmationDialog()
                leaveTheQueue()
            }

            is OnQueueItemClicked -> getQueueItemDetails(userId = event.userId)

            is QueueEvent.OnUserPhotoClicked -> viewModelScope.launch {
                _action.send(QueueAction.NavigateToFullSizePhotoScreen(photo = event.photo))
            }

            is OnQueueReordered -> when (event.line) {
                Line.LEFT -> _state.update {
                    it.copy(
                        leftQueue = it.leftQueue
                            .toMutableList()
                            .apply { add(event.to, removeAt(event.from)) }
                            .toImmutableList(),
                        isQueueReordered = true
                    )
                }

                Line.RIGHT -> _state.update {
                    it.copy(
                        rightQueue = it.rightQueue
                            .toMutableList()
                            .apply { add(event.to, removeAt(event.from)) }
                            .toImmutableList(),
                        isQueueReordered = true
                    )
                }
            }

            OnSaveReorderedQueueClicked -> viewModelScope.launch {
                val reorderedQueueItems = mutableListOf<ReorderedQueueItem>()

                state.value.leftQueue.forEach { item ->
                    val newPosition = state.value.leftQueue.indexOf(item).toLong() + 1L

                    if (item.position != newPosition) {
                        reorderedQueueItems.add(
                            ReorderedQueueItem(
                                queueItemId = item.id,
                                newPosition = newPosition
                            )
                        )
                    }
                }
                state.value.rightQueue.forEach { item ->
                    val newPosition = state.value.rightQueue.indexOf(item).toLong() + 1L

                    if (item.position != newPosition) {
                        reorderedQueueItems.add(
                            ReorderedQueueItem(
                                queueItemId = item.id,
                                newPosition = newPosition
                            )
                        )
                    }
                }
                queueRepository.reorderQueue(reorderedQueueItems = reorderedQueueItems).onSuccess {
                    _state.update { it.copy(isQueueReordered = false) }
                }.onFailure { error ->
                    showSocketError(error = error)
                }
            }

            OnQueueItemDetailsDialogDismissRequest -> _state.update { it.copy(userProfile = null) }
        }
    }

    private fun initSession() {
        viewModelScope.launch {
            _state.update { it.copy(isSessionStarting = true) }

            queueRepository.initSession().onSuccess {
                getQueue()
                observeQueue()
            }.onFailure { error ->
                _action.send(ShowError(errorMessage = error.asString()))
            }
            _state.update { it.copy(isSessionStarting = false) }
        }
    }

    private fun observeQueue() {
        queueObserverJob?.cancel()

        queueObserverJob = viewModelScope.launch {
            queueRepository.observeQueue(
                updateQueue = { newQueue ->
                    val (leftQueue, rightQueue) = newQueue.partition { it.line == Line.LEFT }

                    _state.update {
                        it.copy(
                            leftQueue = leftQueue.toPersistentList(),
                            rightQueue = rightQueue.toPersistentList(),
                            isLoading = false
                        )
                    }
                },
                onError = { error ->
                    showSocketError(error = error)
                    _state.update { it.copy(isLoading = false) }
                }
            )
        }
    }

    private fun getQueue() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            queueRepository.getQueue().onSuccess { queue ->
                val (leftQueue, rightQueue) = queue.partition { it.line == Line.LEFT }

                _state.update {
                    it.copy(
                        leftQueue = leftQueue.toPersistentList(),
                        rightQueue = rightQueue.toPersistentList()
                    )
                }
            }.onFailure { error ->
                _action.send(ShowError(errorMessage = error.asString()))
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun requestNotificationPermission(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(REMOTE_NOTIFICATION)
                onSuccess()
            } catch (deniedAlwaysException: DeniedAlwaysException) {
                _action.send(
                    ShowPermissionError(
                        errorMessage = getString(Res.string.notification_permission_error)
                    )
                )
            } catch (deniedException: DeniedException) {
                _action.send(
                    ShowPermissionError(
                        errorMessage = getString(Res.string.notification_permission_error)
                    )
                )
            }
        }
    }

    private fun joinTheQueue(userId: Int, line: Line) {
        viewModelScope.launch {
            val permissionState = permissionsController.getPermissionState(REMOTE_NOTIFICATION)

            if (permissionState != PermissionState.Granted) {
                requestNotificationPermission(
                    onSuccess = { joinTheQueue(userId = userId, line = line) }
                )
                return@launch
            }
            _state.update { it.copy(isLoading = true) }

            val isUserSubscribed = isUserSubscribed(userId = userId).getOrNull() ?: false

            if (!isUserSubscribed) {
                _action.send(QueueAction.NavigateToPaywallScreen)
                return@launch
            }
            canJoinTheQueue(userId = userId, line = line).onSuccess {
                queueRepository.joinTheQueue(
                    userId = userId,
                    line = line,
                    notificationToken = NotifierManager.getPushNotifier().getToken()
                ).onFailure { error ->
                    showSocketError(error = error)
                    _state.update { it.copy(isLoading = false) }
                }
            }.onFailure { queueError ->
                _action.send(ShowError(errorMessage = queueError.asString()))
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun leaveTheQueue() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            queueRepository.leaveTheQueue(
                queueItemId = state.value.queueItemIdToDelete ?: return@launch
            ).onFailure { error ->
                showSocketError(error = error)
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun getQueueItemDetails(userId: Int) {
        viewModelScope.launch {
            getQueueItemDetailsUseCase(userId = userId).onSuccess { userProfile ->
                _state.update { it.copy(userProfile = userProfile) }
            }.onFailure { error ->
                _action.send(ShowError(errorMessage = error.asString()))
            }
        }
    }

    private fun toggleLeaveQueueConfirmationDialog() {
        _state.update {
            it.copy(showLeaveQueueConfirmationDialog = !it.showLeaveQueueConfirmationDialog)
        }
    }

    private suspend fun showSocketError(error: DataError.Socket) {
        _action.send(
            ShowError(
                errorMessage = error.asString(),
                actionLabel = getString(resource = Res.string.reconnect).takeIf {
                    error == DataError.Socket.SERVER_CONNECTION_LOST
                }
            )
        )
    }

    private fun canJoinTheQueue(userId: Int, line: Line): Result<Unit, JoinQueueError> {
        val leftQueue = state.value.leftQueue
        val rightQueue = state.value.rightQueue

        val userItemInLeftQueue = leftQueue.find { it.userId == userId }
        val userItemInRightQueue = rightQueue.find { it.userId == userId }

        if (userItemInLeftQueue == null && userItemInRightQueue == null) {
            return Result.Success(Unit)
        }
        val userPositionInLeftQueue = leftQueue.indexOf(userItemInLeftQueue)
        val userPositionInRightQueue = rightQueue.indexOf(userItemInRightQueue)

        return if (line == Line.LEFT && userItemInLeftQueue != null)
            Result.Error(JoinQueueError.ALREADY_IN_QUEUE_ERROR)
        else if (line == Line.RIGHT && userItemInRightQueue != null)
            Result.Error(JoinQueueError.ALREADY_IN_QUEUE_ERROR)
        else if (line == Line.LEFT && userItemInRightQueue != null) {
            if (leftQueue.size - userPositionInRightQueue >= 4) Result.Success(Unit)
            else if (userPositionInRightQueue - leftQueue.size >= 4) Result.Success(Unit)
            else Result.Error(JoinQueueError.INTERVAL_ERROR)
        } else if (line == Line.RIGHT && userItemInLeftQueue != null) {
            if (rightQueue.size - userPositionInLeftQueue >= 4) Result.Success(Unit)
            else if (userPositionInLeftQueue - rightQueue.size >= 4) Result.Success(Unit)
            else Result.Error(JoinQueueError.INTERVAL_ERROR)
        } else Result.Error(JoinQueueError.UNKNOWN_ERROR)
    }

    sealed interface QueueAction {
        data class NavigateToQueueAdminScreen(val line: Line) : QueueAction
        data class NavigateToFullSizePhotoScreen(val photo: String) : QueueAction
        data object NavigateToPaywallScreen : QueueAction

        data class ShowError(
            val errorMessage: String,
            val actionLabel: String? = null
        ) : QueueAction

        data class ShowPermissionError(val errorMessage: String) : QueueAction
    }
}