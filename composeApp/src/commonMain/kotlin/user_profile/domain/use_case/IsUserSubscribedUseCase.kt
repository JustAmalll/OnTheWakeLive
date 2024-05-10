package user_profile.domain.use_case


import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.repository.UserProfileRepository

class IsUserSubscribedUseCase(
    private val userProfileRepository: UserProfileRepository
) {

    suspend operator fun invoke(userId: Int): Result<Boolean, DataError.Network> =
        userProfileRepository.isUserSubscribed(userId = userId)
}