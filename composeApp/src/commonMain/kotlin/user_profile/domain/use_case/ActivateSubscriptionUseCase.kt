package user_profile.domain.use_case

import com.benasher44.uuid.Uuid
import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.repository.UserProfileRepository

class ActivateSubscriptionUseCase(
    private val userProfileRepository: UserProfileRepository
) {

    suspend operator fun invoke(userId: Uuid): Result<Unit, DataError.Network> =
        userProfileRepository.activateUserSubscription(userId = userId)
}