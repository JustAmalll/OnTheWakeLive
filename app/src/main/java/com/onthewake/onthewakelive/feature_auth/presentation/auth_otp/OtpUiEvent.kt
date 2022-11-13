package com.onthewake.onthewakelive.feature_auth.presentation.auth_otp

sealed class OtpUiEvent {
    data class OtpCodeChanged(val value: String) : OtpUiEvent()
    object VerifyOtpAndSignUp : OtpUiEvent()
}