package auth.presentation.login

import androidx.compose.runtime.Stable

@Stable
data class LoginState(
    val isLoading: Boolean = false,

    val phoneNumber: String = "",
    val phoneNumberError: String? = null,

    val password: String = "",
    val passwordError: String? = null
)