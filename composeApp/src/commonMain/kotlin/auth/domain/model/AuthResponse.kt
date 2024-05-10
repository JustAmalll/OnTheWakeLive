package auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("userId") val userId: Int,
    @SerialName("token") val token: String,
    @SerialName("isAdmin") val isAdmin: Boolean
)