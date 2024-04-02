package user_profile.domain.use_case

import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.model.UserProfile
import user_profile.domain.repository.UserProfileRepository

class GetUserProfileUseCase(
    private val userProfileRepository: UserProfileRepository
) {

    suspend operator fun invoke(): Result<UserProfile, DataError.Network> =
        userProfileRepository.getUserProfile()
}