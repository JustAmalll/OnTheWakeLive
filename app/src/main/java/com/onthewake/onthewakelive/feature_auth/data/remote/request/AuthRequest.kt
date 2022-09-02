package com.onthewake.onthewakelive.feature_auth.data.remote.request

import androidx.annotation.Keep

@Keep
data class AuthRequest(
    val phoneNumber: String,
    val password: String
)