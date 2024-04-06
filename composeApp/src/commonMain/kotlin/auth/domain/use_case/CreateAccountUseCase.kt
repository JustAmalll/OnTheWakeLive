package auth.domain.use_case

import auth.domain.model.AuthResponse
import auth.domain.model.CreateAccountRequest
import auth.domain.repository.AuthRepository
import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.onSuccess

class CreateAccountUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        password: String
    ): Result<AuthResponse, DataError.Network> = authRepository.createAccount(
        createAccountRequest = CreateAccountRequest(
            firstName = firstName.trim(),
            lastName = lastName.trim(),
            phoneNumber = phoneNumber.trim(),
            password = password.trim()
        )
    ).onSuccess { response ->
        authRepository.cacheAuthResponse(authResponse = response)
    }
}