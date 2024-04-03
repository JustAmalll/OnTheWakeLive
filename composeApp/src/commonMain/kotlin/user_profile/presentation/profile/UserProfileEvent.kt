package user_profile.presentation.profile

import queue.presentation.list.QueueEvent

sealed interface UserProfileEvent {
    data object OnEditProfileClicked: UserProfileEvent
    data object OnUserPhotoClicked : UserProfileEvent
    data object OnLogoutClicked: UserProfileEvent
}