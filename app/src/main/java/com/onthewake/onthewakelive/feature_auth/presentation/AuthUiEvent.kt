package com.onthewake.onthewakelive.feature_auth.presentation

sealed class AuthUiEvent {
    data class SignUpFirstNameChanged(val value: String): AuthUiEvent()
    data class SignUpLastNameChanged(val value: String): AuthUiEvent()
    data class SignUpPhoneNumberChanged(val value: String): AuthUiEvent()
    data class SignUpPasswordChanged(val value: String): AuthUiEvent()
    object SignUp: AuthUiEvent()

    data class SignInPhoneNumberChanged(val value: String): AuthUiEvent()
    data class SignInPasswordChanged(val value: String): AuthUiEvent()
    object SignIn: AuthUiEvent()
}