package queue.presentation.list

import com.benasher44.uuid.Uuid
import queue.domain.model.Line

@Suppress("ArrayInDataClass")
sealed interface QueueEvent {
    data class OnJoinClicked(val line: Line, val isUserAdmin: Boolean) : QueueEvent
    data class OnQueueLeaved(val queueItemId: Uuid) : QueueEvent
    data object OnLeaveQueueConfirmed : QueueEvent
    data object LeaveQueueConfirmationDialogDismissRequest : QueueEvent
    data class OnQueueItemClicked(val userId: Uuid) : QueueEvent
    data class OnUserPhotoClicked(val byteArray: ByteArray) : QueueEvent
    data class OnQueueReordered(val from: Int, val to: Int) : QueueEvent
    data object OnSaveReorderedQueueClicked : QueueEvent
}