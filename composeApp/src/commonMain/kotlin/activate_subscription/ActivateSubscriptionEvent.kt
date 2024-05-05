package activate_subscription

import user_profile.domain.model.UserProfile

sealed interface ActivateSubscriptionEvent {
    data object OnNavigateBackClicked : ActivateSubscriptionEvent
    data class OnSearchQueueChanged(val value: String) : ActivateSubscriptionEvent
    data class OnUserSelected(val user: UserProfile) : ActivateSubscriptionEvent
    data class OnUserPhotoClicked(val photo: String) : ActivateSubscriptionEvent
    data object OnChangeSelectedUserClicked : ActivateSubscriptionEvent
    data object OnActivateSubscriptionClicked : ActivateSubscriptionEvent
}