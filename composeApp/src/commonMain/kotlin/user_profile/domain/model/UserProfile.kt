@file:Suppress("ArrayInDataClass")

package user_profile.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    @SerialName("userId") val userId: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("photo") val photo: ByteArray?,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("telegram") val telegram: String?,
    @SerialName("instagram") val instagram: String?
)