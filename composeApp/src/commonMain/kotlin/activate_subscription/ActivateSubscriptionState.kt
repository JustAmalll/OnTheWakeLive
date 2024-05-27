package activate_subscription

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import user_profile.domain.model.UserProfile

data class ActivateSubscriptionState(
    val isUserSearching: Boolean = false,
    val searchQuery: String = "",
    val searchQueryError: String? = null,
    val selectedUser: UserProfile? = null,
    val searchedUsers: ImmutableList<UserProfile> = persistentListOf()
)