package auth.domain.use_case

import auth.domain.repository.AuthRepository
import com.benasher44.uuid.Uuid

class GetUserIdUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(): Uuid? = authRepository.getUserId()
}