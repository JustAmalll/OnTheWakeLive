package com.onthewake.onthewakelive.feature_auth.domain.repository

import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult

interface AuthRepository {

    suspend fun signUp(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        password: String
    ): AuthResult<Unit>

    suspend fun signIn(
        phoneNumber: String,
        password: String
    ): AuthResult<Unit>

    suspend fun authenticate(): AuthResult<Unit>
}