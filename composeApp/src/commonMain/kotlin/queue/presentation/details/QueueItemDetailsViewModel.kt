package queue.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import queue.domain.repository.QueueRepository

class QueueItemDetailsViewModel(
    private val queueRepository: QueueRepository,
    private val queueItemId: String
) : ViewModel() {

    private val _state = MutableStateFlow(QueueItemDetailsState())
    val state: StateFlow<QueueItemDetailsState> = _state.asStateFlow()

    init {
        getQueueItemDetails()
    }

    private fun getQueueItemDetails() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            queueRepository.getQueueItemDetails(
                queueItemId = queueItemId
            ).onSuccess { userProfile ->
                _state.update { it.copy(userProfile = userProfile) }
            }.onFailure {
                it.printStackTrace()
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}