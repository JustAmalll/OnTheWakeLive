package queue.presentation.list

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import queue.domain.module.QueueItem

@Stable
data class QueueState(
    val isUserAdmin: Boolean = false,
    val queue: ImmutableList<QueueItem> = persistentListOf(),
    val showLeaveQueueConfirmationDialog: Boolean = false,
    val queueItemIdToDelete: String? = null
)