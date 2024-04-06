package queue.presentation.admin

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import queue.domain.model.Line
import user_profile.domain.model.UserProfile

@Stable
data class QueueAdminState(
    val isUserSearching: Boolean = false,
    val firstName: String = "",
    val firstNameError: String? = null,
    val selectedUser: UserProfile? = null,
    val line: Line = Line.RIGHT,
    val searchedUsers: ImmutableList<UserProfile> = persistentListOf()
)