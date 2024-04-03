package queue.presentation.list

import queue.domain.module.Line

@Suppress("ArrayInDataClass")
sealed interface QueueEvent {
    data class OnJoinClicked(val line: Line) : QueueEvent
    data class OnQueueLeaved(val queueItemId: String) : QueueEvent
    data object OnLeaveQueueConfirmed : QueueEvent
    data object LeaveQueueConfirmationDialogDismissRequest : QueueEvent
    data class OnQueueItemClicked(val queueItemId: String) : QueueEvent
    data class OnUserPhotoClicked(val byteArray: ByteArray) : QueueEvent
}