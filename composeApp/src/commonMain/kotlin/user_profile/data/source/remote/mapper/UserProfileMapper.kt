package user_profile.data.source.remote.mapper

import user_profile.data.source.remote.model.UpdateUserProfileRequest
import user_profile.domain.model.UserProfile
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun UserProfile.toUpdateUserProfileRequest() = UpdateUserProfileRequest(
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    photo = photo?.let { Base64.encode(source = photo) },
    phoneNumber = phoneNumber,
    telegram = telegram,
    instagram = instagram
)