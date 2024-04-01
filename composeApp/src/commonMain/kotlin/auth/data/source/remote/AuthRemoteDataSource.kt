package auth.data.source.remote

import auth.domain.model.AuthResponse
import auth.domain.model.CreateAccountRequest
import auth.domain.model.LoginRequest
import core.domain.utils.NetworkError
import core.domain.utils.Result

interface AuthRemoteDataSource {
    suspend fun authenticate(): Result<Unit, NetworkError>
    suspend fun login(loginRequest: LoginRequest): Result<AuthResponse, NetworkError>

    suspend fun createAccount(
        createAccountRequest: CreateAccountRequest
    ): Result<AuthResponse, NetworkError>
}