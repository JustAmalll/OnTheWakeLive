package user_profile.presentation

sealed interface UserProfileEvent {
    data class OnFirstNameChanged(val value: String) : UserProfileEvent
    data class OnLastNameChanged(val value: String) : UserProfileEvent
    data class OnTelegramChanged(val value: String) : UserProfileEvent
    data class OnInstagramChanged(val value: String) : UserProfileEvent

    @Suppress("ArrayInDataClass")
    data class OnUserPhotoSelected(val byteArray: ByteArray) : UserProfileEvent

    @Suppress("ArrayInDataClass")
    data class OnUserPhotoCropped(val byteArray: ByteArray) : UserProfileEvent

    data object OnDeleteUserPhotoClicked: UserProfileEvent

    data object OnEditProfileClicked : UserProfileEvent
    data object OnUserPhotoClicked : UserProfileEvent
    data object OnLogoutClicked : UserProfileEvent
}