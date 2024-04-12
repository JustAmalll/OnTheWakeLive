package queue.presentation.list

import androidx.compose.runtime.Stable
import com.benasher44.uuid.Uuid
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import queue.domain.model.QueueItem

@Stable
data class QueueState(
    val isLoading: Boolean = false,
    val isSessionStarting: Boolean = false,

    val isUserAdmin: Boolean = false,
    val queue: ImmutableList<QueueItem> = persistentListOf(),
    val showLeaveQueueConfirmationDialog: Boolean = false,
    val queueItemIdToDelete: Uuid? = null,
    val isQueueReordered: Boolean = false
)