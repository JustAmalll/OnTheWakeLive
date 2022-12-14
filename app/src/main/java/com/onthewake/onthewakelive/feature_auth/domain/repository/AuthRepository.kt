package com.onthewake.onthewakelive.feature_auth.domain.repository

import android.app.Activity
import com.onthewake.onthewakelive.feature_auth.data.remote.request.AuthRequest
import com.onthewake.onthewakelive.feature_auth.data.remote.request.CreateAccountRequest
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult

interface AuthRepository {
    suspend fun signUp(accountRequest: CreateAccountRequest): AuthResult<Unit>
    suspend fun signIn(authRequest: AuthRequest): AuthResult<Unit>
    suspend fun verifyOtp(otp: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
    suspend fun isUserAlreadyExists(phoneNumber: String): Boolean
    suspend fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        isResendAction: Boolean
    ): AuthResult<Unit>
}