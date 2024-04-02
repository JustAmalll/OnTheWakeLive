package user_profile.presentation

sealed interface UserProfileEvent {
    data object OnEditProfileClicked: UserProfileEvent
    data object OnLogoutClicked: UserProfileEvent
}