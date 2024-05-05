package activate_subscription

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import user_profile.domain.model.UserProfile

data class ActivateSubscriptionState(
    val isUserSearching: Boolean = false,
    val firstName: String = "",
    val firstNameError: String? = null,
    val selectedUser: UserProfile? = null,
    val searchedUsers: ImmutableList<UserProfile> = persistentListOf()
)