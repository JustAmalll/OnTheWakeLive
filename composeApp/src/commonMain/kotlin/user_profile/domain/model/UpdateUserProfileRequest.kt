package user_profile.domain.model

import com.benasher44.uuid.Uuid
import core.domain.utils.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileRequest(
    @Serializable(with = UuidSerializer::class)
    @SerialName("userId") val userId: Uuid,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String,
    @SerialName("telegram") val telegram: String?,
    @SerialName("instagram") val instagram: String?,
    @SerialName("photo") val photo: String?
)