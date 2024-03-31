package queue.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import queue.domain.module.Line
import queue.domain.module.QueueSocketResponse.Join
import queue.domain.module.QueueSocketResponse.Remove
import queue.domain.repository.QueueRepository
import queue.presentation.list.QueueEvent.LeaveQueueConfirmationDialogDismissRequest
import queue.presentation.list.QueueEvent.OnQueueItemClicked
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToQueueItemDetails

class QueueViewModel(
    private val queueRepository: QueueRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QueueState())
    val state: StateFlow<QueueState> = _state.asStateFlow()

    private val _action = Channel<QueueAction>()
    val actions: Flow<QueueAction> = _action.receiveAsFlow()

    init {
        initSession()
    }

    fun onEvent(event: QueueEvent) {
        when (event) {
            is QueueEvent.OnJoinClicked -> joinTheQueue(line = event.line)

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
                _action.send(NavigateToQueueItemDetails(queueItemId = event.queueItemId))
            }

            is QueueEvent.OnUserPhotoClicked -> {}
        }
    }

    private fun initSession() {
        viewModelScope.launch {
            queueRepository.initSession().onSuccess {
                getQueue()
                observeQueue()
            }
        }
    }

    private fun observeQueue() {
        viewModelScope.launch {
            queueRepository.observeQueue().collect { response ->
                _state.update { state ->
                    state.copy(queue = state.queue.toMutableList().apply {
                        when (response) {
                            is Join -> add(response.queueItem)
                            is Remove -> removeAll { it.id == response.queueItemId }
                        }
                    }.toPersistentList())
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
            queueRepository.joinTheQueue(line = line).onFailure { exception ->
                exception.printStackTrace()
            }
        }
    }

    private fun leaveTheQueue() {
        viewModelScope.launch {
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
        data class NavigateToQueueItemDetails(val queueItemId: String) : QueueAction
    }
}