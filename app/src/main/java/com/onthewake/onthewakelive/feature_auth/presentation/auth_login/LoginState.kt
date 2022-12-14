package com.onthewake.onthewakelive.feature_auth.presentation.auth_login

data class LoginState(
    val isLoading: Boolean = false,
    val signInPhoneNumber: String = "",
    val signInPhoneNumberError: String? = null,
    val signInPassword: String = "",
    val signInPasswordError: String? = null
)