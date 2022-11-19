package com.onthewake.onthewakelive.feature_profile.presentation.edit_profile

data class EditProfileState(
    val isLoading: Boolean = false,
    val firstName: String = "",
    val profileFirsNameError: String? = null,
    val lastName: String = "",
    val profileLastNameError: String? = null,
    val phoneNumber: String = "",
    val profilePhoneNumberError: String? = null,
    val telegram: String = "",
    val instagram: String = "",
    val profilePictureUri: String = "",
    val dateOfBirth: String = "",
    val profileDateOfBirthError: String? = null
)