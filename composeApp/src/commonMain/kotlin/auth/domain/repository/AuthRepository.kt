package auth.domain.repository

import auth.domain.model.AuthResponse
import auth.domain.model.CreateAccountRequest
import auth.domain.model.LoginRequest

import core.domain.utils.DataError
import core.domain.utils.Result

interface AuthRepository {
    suspend fun authenticate(): Result<Unit, DataError.Network>
    suspend fun login(loginRequest: LoginRequest): Result<AuthResponse, DataError.Network>
    suspend fun cacheAuthResponse(authResponse: AuthResponse)
    suspend fun isUserAlreadyExists(phoneNumber: String): Result<Boolean, DataError.Network>
    suspend fun isUserAdmin(): Boolean
    suspend fun getUserId(): Int?

    suspend fun createAccount(
        createAccountRequest: CreateAccountRequest
    ): Result<AuthResponse, DataError.Network>

    suspend fun logout()
}