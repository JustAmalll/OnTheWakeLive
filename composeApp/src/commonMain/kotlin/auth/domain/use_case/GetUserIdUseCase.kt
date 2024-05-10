package auth.domain.use_case

import auth.domain.repository.AuthRepository


class GetUserIdUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(): Int? = authRepository.getUserId()
}