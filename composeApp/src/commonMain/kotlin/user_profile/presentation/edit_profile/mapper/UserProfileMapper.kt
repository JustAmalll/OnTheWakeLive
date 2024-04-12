package user_profile.presentation.edit_profile.mapper

import user_profile.domain.model.UserProfile
import user_profile.presentation.edit_profile.EditUserProfileState

fun UserProfile.toEditUserProfileState(state: EditUserProfileState) = state.copy(
    firstName = firstName,
    lastName = lastName,
    phoneNumber = phoneNumber,
    instagram = instagram ?: "",
    telegram = telegram ?: "",
    photo = photo
)