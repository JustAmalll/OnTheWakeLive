package queue.presentation.list


import queue.domain.model.Line

sealed interface QueueEvent {
    data object ConnectToSession: QueueEvent
    data class OnJoinClicked(val userId: Int, val line: Line, val isUserAdmin: Boolean) : QueueEvent
    data class OnQueueLeaved(val queueItemId: Int) : QueueEvent
    data object OnLeaveQueueConfirmed : QueueEvent
    data object LeaveQueueConfirmationDialogDismissRequest : QueueEvent
    data class OnQueueItemClicked(val userId: Int) : QueueEvent
    data class OnUserPhotoClicked(val photo: String) : QueueEvent
    data class OnQueueReordered(val line: Line, val from: Int, val to: Int) : QueueEvent
    data object OnSaveReorderedQueueClicked : QueueEvent
}