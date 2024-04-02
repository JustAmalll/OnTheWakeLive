package auth.data.repository

import auth.data.source.cache.AuthCacheDataSource
import auth.data.source.remote.AuthRemoteDataSource
import auth.domain.model.AuthResponse
import auth.domain.model.CreateAccountRequest
import auth.domain.model.LoginRequest
import auth.domain.repository.AuthRepository
import core.domain.utils.DataError
import core.domain.utils.Result

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authCacheDataSource: AuthCacheDataSource
) : AuthRepository {

    override suspend fun authenticate(): Result<Unit, DataError.Network> =
        authRemoteDataSource.authenticate()

    override suspend fun login(
        loginRequest: LoginRequest
    ): Result<AuthResponse, DataError.Network> = authRemoteDataSource.login(
        loginRequest = loginRequest
    )

    override suspend fun createAccount(
        createAccountRequest: CreateAccountRequest
    ): Result<AuthResponse, DataError.Network> = authRemoteDataSource.createAccount(
        createAccountRequest = createAccountRequest
    )

    override suspend fun cacheJwtTokenAndUserId(token: String, userId: String) =
        authCacheDataSource.cacheJwtTokenAndUserId(token = token, userId = userId)

    override suspend fun getUserId(): String? =
        authCacheDataSource.getUserId()


    override suspend fun logout() =
        authCacheDataSource.logout()
}