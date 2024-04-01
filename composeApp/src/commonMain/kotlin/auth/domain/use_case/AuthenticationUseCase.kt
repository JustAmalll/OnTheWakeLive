package auth.domain.use_case

import auth.domain.repository.AuthRepository
import core.domain.utils.NetworkError
import core.domain.utils.Result

class AuthenticationUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Result<Unit, NetworkError> =
        authRepository.authenticate()
}