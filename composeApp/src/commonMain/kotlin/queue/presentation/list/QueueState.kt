package queue.presentation.list

import androidx.compose.runtime.Stable

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import queue.domain.model.QueueItem
import user_profile.domain.model.UserProfile

@Stable
data class QueueState(
    val isLoading: Boolean = false,
    val isConnected: Boolean = false,
    val isSessionStarting: Boolean = false,

    val leftQueue: ImmutableList<QueueItem> = persistentListOf(),
    val rightQueue: ImmutableList<QueueItem> = persistentListOf(),
    val showLeaveQueueConfirmationDialog: Boolean = false,
    val queueItemIdToDelete: Int? = null,
    val isQueueReordered: Boolean = false,

    val userProfile: UserProfile? = null
)