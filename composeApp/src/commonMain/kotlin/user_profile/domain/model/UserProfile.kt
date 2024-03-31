package user_profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("userId") val userId: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("profilePictureUri") val profilePictureUri: String,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("telegram") val telegram: String,
    @SerialName("instagram") val instagram: String,
    @SerialName("dateOfBirth") val dateOfBirth: String
)
