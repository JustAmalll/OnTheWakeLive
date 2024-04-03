package user_profile.data.source.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileRequest(
    @SerialName("userId") val userId: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("photo") val photo: String?,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("telegram") val telegram: String?,
    @SerialName("instagram") val instagram: String?
)