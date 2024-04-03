package queue.presentation.details

sealed interface QueueItemDetailsEvent {
    data object OnNavigateBackClicked: QueueItemDetailsEvent
    data object OnPhotoClicked: QueueItemDetailsEvent
}