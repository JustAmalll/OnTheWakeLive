package auth.domain.model

import com.benasher44.uuid.Uuid
import core.domain.utils.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @Serializable(with = UuidSerializer::class)
    @SerialName("userId") val userId: Uuid,
    @SerialName("token") val token: String,
    @SerialName("isAdmin") val isAdmin: Boolean
)