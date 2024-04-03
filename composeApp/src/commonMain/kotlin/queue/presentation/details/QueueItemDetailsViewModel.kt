package queue.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import queue.domain.repository.QueueRepository
import queue.presentation.details.QueueItemDetailsEvent.OnNavigateBackClicked
import queue.presentation.details.QueueItemDetailsViewModel.QueueItemDetailsAction.NavigateBack
import queue.presentation.details.QueueItemDetailsViewModel.QueueItemDetailsAction.NavigateToFullSizePhotoScreen

class QueueItemDetailsViewModel(
    private val queueRepository: QueueRepository,
    private val queueItemId: String
) : ViewModel() {

    private val _state = MutableStateFlow(QueueItemDetailsState())
    val state: StateFlow<QueueItemDetailsState> = _state.asStateFlow()

    private val _action = Channel<QueueItemDetailsAction>()
    val actions: Flow<QueueItemDetailsAction> = _action.receiveAsFlow()

    init {
        getQueueItemDetails()
    }

    fun onEvent(event: QueueItemDetailsEvent) {
        when (event) {
            OnNavigateBackClicked -> viewModelScope.launch {
                _action.send(NavigateBack)
            }

            QueueItemDetailsEvent.OnPhotoClicked -> viewModelScope.launch {
                state.value.userProfile?.photo?.let { photo ->
                    _action.send(NavigateToFullSizePhotoScreen(photo = photo))
                }
            }
        }
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

    sealed interface QueueItemDetailsAction {
        data object NavigateBack : QueueItemDetailsAction

        @Suppress("ArrayInDataClass")
        data class NavigateToFullSizePhotoScreen(val photo: ByteArray) : QueueItemDetailsAction
    }
}