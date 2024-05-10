package auth.domain.use_case

import auth.domain.repository.AuthRepository

class IsUserAlreadyExistsUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(phoneNumber: String) =
        authRepository.isUserAlreadyExists(phoneNumber = phoneNumber)
}