package auth.domain.repository

import auth.domain.model.AuthResponse
import auth.domain.model.CreateAccountRequest
import auth.domain.model.LoginRequest
import core.domain.utils.NetworkError
import core.domain.utils.Result

interface AuthRepository {
    suspend fun authenticate(): Result<Unit, NetworkError>
    suspend fun login(loginRequest: LoginRequest): Result<AuthResponse, NetworkError>
    suspend fun cacheJwtTokenAndUserId(token: String, userId: String)
    suspend fun getUserId(): String?

    suspend fun createAccount(
        createAccountRequest: CreateAccountRequest
    ): Result<AuthResponse, NetworkError>

    suspend fun logout()
}