package auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("password") val password: String
)