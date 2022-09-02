package com.onthewake.onthewakelive.feature_auth.data.remote.request

import androidx.annotation.Keep

@Keep
data class CreateAccountRequest(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val telegram: String,
    val instagram: String,
    val password: String
)