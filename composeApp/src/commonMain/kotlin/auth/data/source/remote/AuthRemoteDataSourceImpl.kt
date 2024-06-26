package auth.data.source.remote

import auth.domain.model.AuthResponse
import auth.domain.model.CreateAccountRequest
import auth.domain.model.LoginRequest
import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.runCatchingNetwork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AuthRemoteDataSourceImpl(
    private val client: HttpClient
) : AuthRemoteDataSource {

    override suspend fun authenticate(): Result<Unit, DataError.Network> =
        withContext(Dispatchers.IO) {
            runCatchingNetwork {
                client.get("/authenticate")
                Unit
            }
        }

    override suspend fun login(
        loginRequest: LoginRequest
    ): Result<AuthResponse, DataError.Network> = withContext(Dispatchers.IO) {
        runCatchingNetwork {
            client.post("/login") {
                setBody(loginRequest)
            }.body<AuthResponse>()
        }
    }

    override suspend fun isUserAlreadyExists(
        phoneNumber: String
    ): Result<Boolean, DataError.Network> = withContext(Dispatchers.IO) {
        runCatchingNetwork {
            client.get("/isUserAlreadyExists") {
                parameter("phoneNumber", phoneNumber)
            }.body<Boolean>()
        }
    }

    override suspend fun createAccount(
        createAccountRequest: CreateAccountRequest
    ): Result<AuthResponse, DataError.Network> = withContext(Dispatchers.IO) {
        runCatchingNetwork {
            client.post("/create-account") {
                setBody(createAccountRequest)
            }.body<AuthResponse>()
        }
    }
}