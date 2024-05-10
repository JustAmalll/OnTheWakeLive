package user_profile.domain.use_case

import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.repository.UserProfileRepository

class ActivateSubscriptionUseCase(
    private val userProfileRepository: UserProfileRepository
) {

    suspend operator fun invoke(userId: Int): Result<Unit, DataError.Network> =
        userProfileRepository.activateUserSubscription(userId = userId)
}