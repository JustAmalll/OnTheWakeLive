package com.onthewake.onthewakelive.feature_profile.domain.module

data class UpdateProfileData(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val instagram: String,
    val telegram: String,
    val dateOfBirth: String,
    val profilePictureUri: String
)
