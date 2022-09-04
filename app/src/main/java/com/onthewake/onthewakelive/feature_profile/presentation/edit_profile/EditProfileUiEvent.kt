package com.onthewake.onthewakelive.feature_profile.presentation.edit_profile

import android.net.Uri

sealed class EditProfileUiEvent {
    data class EditProfileFirstNameChanged(val value: String): EditProfileUiEvent()
    data class EditProfileLastNameChanged(val value: String): EditProfileUiEvent()
    data class EditProfilePhoneNumberChanged(val value: String): EditProfileUiEvent()
    data class EditProfileTelegramChanged(val value: String): EditProfileUiEvent()
    data class EditProfileInstagramChanged(val value: String): EditProfileUiEvent()
    object EditEditProfile: EditProfileUiEvent()

    data class PickImage(val uri: Uri?) : EditProfileUiEvent()
    data class CropImage(val uri: Uri?) : EditProfileUiEvent()
}