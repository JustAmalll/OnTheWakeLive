package user_profile.presentation.profile

sealed interface UserProfileEvent {
    data object OnEditProfileClicked : UserProfileEvent
    data object OnUserPhotoClicked : UserProfileEvent
    data object OnLogoutClicked : UserProfileEvent
}