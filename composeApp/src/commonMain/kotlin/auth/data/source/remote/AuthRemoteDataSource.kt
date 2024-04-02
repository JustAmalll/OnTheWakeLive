package auth.data.source.remote

import auth.domain.model.AuthResponse
import auth.domain.model.CreateAccountRequest
import auth.domain.model.LoginRequest
import core.domain.utils.DataError
import core.domain.utils.Result

interface AuthRemoteDataSource {
    suspend fun authenticate(): Result<Unit, DataError.Network>
    suspend fun login(loginRequest: LoginRequest): Result<AuthResponse, DataError.Network>

    suspend fun createAccount(
        createAccountRequest: CreateAccountRequest
    ): Result<AuthResponse, DataError.Network>
}