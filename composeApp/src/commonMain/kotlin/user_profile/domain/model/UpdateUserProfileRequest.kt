package user_profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileRequest(
    @SerialName("userId") val userId: Int,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("telegram") val telegram: String?,
    @SerialName("instagram") val instagram: String?,
    @SerialName("photo") val photo: String?
)