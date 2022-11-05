package com.onthewake.onthewakelive.feature_auth.presentation

data class AuthState(
    val isLoading: Boolean = false,
    val signUpFirsName: String = "",
    val signUpFirsNameError: String? = null,
    val signUpLastName: String = "",
    val signUpLastNameError: String? = null,
    val signUpPhoneNumber: String = "",
    val signUpPhoneNumberError: String? = null,
    val signUpPassword: String = "",
    val signUpPasswordError: String? = null,
    val signInPhoneNumber: String = "",
    val signInPhoneNumberError: String? = null,
    val signInPassword: String = "",
    val signInPasswordError: String? = null
)