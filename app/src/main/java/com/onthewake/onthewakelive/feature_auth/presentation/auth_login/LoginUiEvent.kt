package com.onthewake.onthewakelive.feature_auth.presentation.auth_login

sealed class LoginUiEvent {
    data class SignInPhoneNumberChanged(val value: String) : LoginUiEvent()
    data class SignInPasswordChanged(val value: String) : LoginUiEvent()
    object SignIn : LoginUiEvent()
}