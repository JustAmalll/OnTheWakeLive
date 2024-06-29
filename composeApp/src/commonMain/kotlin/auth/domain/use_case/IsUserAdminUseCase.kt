package auth.domain.use_case

import auth.domain.repository.AuthRepository

class IsUserAdminUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(): Boolean = authRepository.isUserAdmin()
}