package queue.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benasher44.uuid.Uuid
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import queue.domain.model.Line
import queue.domain.model.QueueSocketResponse.Join
import queue.domain.model.QueueSocketResponse.Remove
import queue.domain.model.QueueSocketResponse.Update
import queue.domain.model.ReorderedQueueItem
import queue.domain.repository.QueueRepository
import queue.presentation.list.QueueEvent.LeaveQueueConfirmationDialogDismissRequest
import queue.presentation.list.QueueEvent.OnQueueItemClicked
import queue.presentation.list.QueueEvent.OnSaveReorderedQueueClicked
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToQueueItemDetails

class QueueViewModel(
    private val queueRepository: QueueRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QueueState())
    val state: StateFlow<QueueState> = _state.asStateFlow()

    private val _action = Channel<QueueAction>()
    val actions: Flow<QueueAction> = _action.receiveAsFlow()

    init {
        getQueue()
        initSession()
    }

    fun onEvent(event: QueueEvent) {
        when (event) {
            is QueueEvent.OnJoinClicked -> {
                if (event.isUserAdmin) {
                   viewModelScope.launch {
                       _action.send(QueueAction.NavigateToQueueAdminScreen(line = event.line))
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

            is QueueEvent.OnUserPhotoClicked -> {}

            is QueueEvent.OnQueueReordered -> {
                _state.update {
                    it.copy(
                        queue = it.queue
                            .toMutableList()
                            .apply { add(event.to, removeAt(event.from)) }
                            .toImmutableList(),
                        isQueueReordered = true
                    )
                }
            }

            OnSaveReorderedQueueClicked -> viewModelScope.launch {
                val reorderedQueueItems: MutableList<ReorderedQueueItem> = mutableListOf()

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
                }
            }
        }
    }

    private fun initSession() {
        viewModelScope.launch {
            queueRepository.initSession().onSuccess {
                observeQueue()
            }
        }
    }

    private fun observeQueue() {
        viewModelScope.launch {
            queueRepository.observeQueue().collect { response ->
                _state.update { state ->
                    state.copy(
                        queue = state.queue.toMutableList().apply {
                            when (response) {
                                is Join -> add(response.queueItem)
                                is Remove -> removeAll { it.id == response.queueItemId }
                                is Update -> {
                                    clear()
                                    addAll(response.queue)
                                }
                            }
                        }.toPersistentList(),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun getQueue() {
        viewModelScope.launch {
            queueRepository.getQueue().onSuccess { queue ->
                _state.update { it.copy(queue = queue.toPersistentList()) }
            }
        }
    }

    private fun joinTheQueue(line: Line) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            queueRepository.joinTheQueue(line = line).onFailure { exception ->
                exception.printStackTrace()
            }
        }
    }

    private fun leaveTheQueue() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            queueRepository.leaveTheQueue(
                queueItemId = state.value.queueItemIdToDelete ?: return@launch
            )
        }
    }

    private fun toggleLeaveQueueConfirmationDialog() {
        _state.update {
            it.copy(showLeaveQueueConfirmationDialog = !it.showLeaveQueueConfirmationDialog)
        }
    }

    sealed interface QueueAction {
        data class NavigateToQueueItemDetails(val userId: Uuid) : QueueAction
        data class NavigateToQueueAdminScreen(val line: Line) : QueueAction
    }
}