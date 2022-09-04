package com.onthewake.onthewakelive.feature_profile.presentation.edit_profile

data class EditProfileState(
    val isLoading: Boolean = false,
    val profileFirsName: String = "",
    val profileFirsNameError: String? = null,
    val profileLastName: String = "",
    val profileLastNameError: String? = null,
    val profilePhoneNumber: String = "",
    val profilePhoneNumberError: String? = null,
    val profileTelegram: String = "",
    val profileTelegramError: String? = null,
    val profileInstagram: String = "",
    val profileInstagramError: String? = null
)