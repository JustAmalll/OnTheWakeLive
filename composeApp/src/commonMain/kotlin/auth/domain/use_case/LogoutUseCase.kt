package auth.domain.use_case

import auth.domain.repository.AuthRepository

class LogoutUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke() {
        authRepository.logout()
    }
}