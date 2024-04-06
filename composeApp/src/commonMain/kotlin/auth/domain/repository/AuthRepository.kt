package auth.domain.repository

import auth.domain.model.AuthResponse
import auth.domain.model.CreateAccountRequest
import auth.domain.model.LoginRequest
import com.benasher44.uuid.Uuid
import core.domain.utils.DataError
import core.domain.utils.Result

interface AuthRepository {
    suspend fun authenticate(): Result<Unit, DataError.Network>
    suspend fun login(loginRequest: LoginRequest): Result<AuthResponse, DataError.Network>
    suspend fun cacheAuthResponse(authResponse: AuthResponse)
    suspend fun isUserAdmin(): Boolean
    suspend fun getUserId(): Uuid?

    suspend fun createAccount(
        createAccountRequest: CreateAccountRequest
    ): Result<AuthResponse, DataError.Network>

    suspend fun logout()
}