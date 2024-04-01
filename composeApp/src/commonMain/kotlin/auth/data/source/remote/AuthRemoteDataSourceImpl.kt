package auth.data.source.remote

import auth.domain.model.AuthResponse
import auth.domain.model.CreateAccountRequest
import auth.domain.model.LoginRequest
import core.domain.utils.NetworkError
import core.domain.utils.Result
import core.domain.utils.runCatching
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthRemoteDataSourceImpl(
    private val client: HttpClient
) : AuthRemoteDataSource {

    override suspend fun authenticate(): Result<Unit, NetworkError> = runCatching {
        client.get("/authenticate")
    }

    override suspend fun login(
        loginRequest: LoginRequest
    ): Result<AuthResponse, NetworkError> = runCatching {
        client.post("/login") {
            setBody(loginRequest)
        }.body<AuthResponse>()
    }

    override suspend fun createAccount(
        createAccountRequest: CreateAccountRequest
    ): Result<AuthResponse, NetworkError> = runCatching {
        client.post("/create-account") {
            setBody(createAccountRequest)
        }.body<AuthResponse>()
    }
}