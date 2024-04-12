package user_profile.presentation.edit_profile

sealed interface EditUserProfileEvent {
    data object OnNavigateBackClicked : EditUserProfileEvent
    data class OnFirstNameChanged(val value: String) : EditUserProfileEvent
    data class OnLastNameChanged(val value: String) : EditUserProfileEvent
    data class OnTelegramChanged(val value: String) : EditUserProfileEvent
    data class OnInstagramChanged(val value: String) : EditUserProfileEvent

    @Suppress("ArrayInDataClass")
    data class OnUserPhotoSelected(val byteArray: ByteArray) : EditUserProfileEvent

    @Suppress("ArrayInDataClass")
    data class OnUserPhotoCropped(val byteArray: ByteArray) : EditUserProfileEvent

    data object OnDeleteUserPhotoClicked: EditUserProfileEvent

    data object OnEditProfileClicked : EditUserProfileEvent
}