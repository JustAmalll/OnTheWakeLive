package queue.presentation.list

import androidx.compose.runtime.Stable
import queue.domain.module.QueueItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import queue.domain.module.Line

@Stable
data class QueueState(
    val isUserAdmin: Boolean = false,
    val queue: ImmutableList<QueueItem> = persistentListOf(),
    val showLeaveQueueConfirmationDialog: Boolean = false,
    val queueItemIdToDelete: String? = null
)