package auth.domain.use_case

import auth.domain.model.LoginRequest
import auth.domain.model.AuthResponse
import auth.domain.repository.AuthRepository
import core.domain.utils.NetworkError
import core.domain.utils.Result
import core.domain.utils.onSuccess

class LoginUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(
        phoneNumber: String,
        password: String
    ): Result<AuthResponse, NetworkError> = authRepository.login(
        loginRequest = LoginRequest(
            phoneNumber = phoneNumber.trim(),
            password = password.trim()
        )
    ).onSuccess { response ->
        authRepository.cacheJwtTokenAndUserId(
            token = response.token,
            userId = response.userId
        )
    }
}