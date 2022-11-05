package com.onthewake.onthewakelive.feature_profile.presentation.edit_profile

data class ProfileStateErrors(
    val isLoading: Boolean = false,
    val profileFirsNameError: String? = null,
    val profileLastNameError: String? = null,
    val profilePhoneNumberError: String? = null,
    val profileDateOfBirthError: String? = null,
)