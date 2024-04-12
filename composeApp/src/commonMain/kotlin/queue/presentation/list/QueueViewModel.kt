package queue.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benasher44.uuid.Uuid
import com.mmk.kmpnotifier.notification.NotifierManager
import core.domain.utils.DataError
import core.domain.utils.asString
import core.domain.utils.onFailure
import core.domain.utils.onSuccess
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
import onthewakelive.composeapp.generated.resources.reconnect
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import queue.domain.model.Line
import queue.domain.model.ReorderedQueueItem
import queue.domain.repository.QueueRepository
import queue.presentation.list.QueueEvent.LeaveQueueConfirmationDialogDismissRequest
import queue.presentation.list.QueueEvent.OnQueueItemClicked
import queue.presentation.list.QueueEvent.OnQueueReordered
import queue.presentation.list.QueueEvent.OnReconnectClicked
import queue.presentation.list.QueueEvent.OnSaveReorderedQueueClicked
import queue.presentation.list.QueueEvent.OnViewAppeared
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToQueueAdminScreen
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToQueueItemDetails
import queue.presentation.list.QueueViewModel.QueueAction.ShowError

@OptIn(ExperimentalResourceApi::class)
class QueueViewModel(private val queueRepository: QueueRepository) : ViewModel() {

    private val _state = MutableStateFlow(QueueState())
    val state: StateFlow<QueueState> = _state.asStateFlow()

    private val _action = Channel<QueueAction>()
    val actions: Flow<QueueAction> = _action.receiveAsFlow()

    private var queueObserverJob: Job? = null

    fun onEvent(event: QueueEvent) {
        when (event) {
            OnViewAppeared -> {
                if (!queueRepository.isSessionActive) {
                    initSession()
                }
                getQueue()
            }

            OnReconnectClicked -> {
                initSession()
                getQueue()
            }

            is QueueEvent.OnJoinClicked -> {
                if (event.isUserAdmin) {
                    viewModelScope.launch {
                        _action.send(NavigateToQueueAdminScreen(line = event.line))
                    }
                } else {
                    joinTheQueue(line = event.line)
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

            is OnQueueItemClicked -> viewModelScope.launch {
                _action.send(NavigateToQueueItemDetails(userId = event.userId))
            }

            is QueueEvent.OnUserPhotoClicked -> viewModelScope.launch {
                _action.send(QueueAction.NavigateToFullSizePhotoScreen(photo = event.photo))
            }

            is OnQueueReordered -> _state.update {
                it.copy(
                    queue = it.queue
                        .toMutableList()
                        .apply { add(event.to, removeAt(event.from)) }
                        .toImmutableList(),
                    isQueueReordered = true
                )
            }

            OnSaveReorderedQueueClicked -> viewModelScope.launch {
                val reorderedQueueItems = mutableListOf<ReorderedQueueItem>()

                state.value.queue.map { item ->
                    val newPosition = state.value.queue.indexOf(item).toLong() + 1L

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
        }
    }

    private fun initSession() {
        viewModelScope.launch {
            _state.update { it.copy(isSessionStarting = true) }

            queueRepository.initSession().onSuccess {
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
                    _state.update {
                        it.copy(queue = newQueue.toPersistentList(), isLoading = false)
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
                _state.update { it.copy(queue = queue.toPersistentList()) }
            }.onFailure { error ->
                _action.send(ShowError(errorMessage = error.asString()))
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun joinTheQueue(line: Line) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            queueRepository.joinTheQueue(
                line = line,
                notificationToken = NotifierManager.getPushNotifier().getToken()
            ).onFailure { error ->
                showSocketError(error = error)
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

    sealed interface QueueAction {
        data class NavigateToQueueItemDetails(val userId: Uuid) : QueueAction
        data class NavigateToQueueAdminScreen(val line: Line) : QueueAction
        data class NavigateToFullSizePhotoScreen(val photo: String) : QueueAction

        data class ShowError(
            val errorMessage: String,
            val actionLabel: String? = null
        ) : QueueAction
    }
}