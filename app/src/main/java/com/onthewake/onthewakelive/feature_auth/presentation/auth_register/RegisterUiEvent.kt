package com.onthewake.onthewakelive.feature_auth.presentation.auth_register

import android.content.Context

sealed class RegisterUiEvent {
    data class SignUpFirstNameChanged(val value: String) : RegisterUiEvent()
    data class SignUpLastNameChanged(val value: String) : RegisterUiEvent()
    data class SignUpPhoneNumberChanged(val value: String) : RegisterUiEvent()
    data class SignUpPasswordChanged(val value: String) : RegisterUiEvent()
    data class SendOtp(val context: Context) : RegisterUiEvent()
}