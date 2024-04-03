package user_profile.domain.use_case

import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.model.UserProfile
import user_profile.domain.repository.UserProfileRepository

class EditProfileUseCase(
    private val userProfileRepository: UserProfileRepository
) {

    suspend operator fun invoke(userProfile: UserProfile): Result<Unit, DataError> =
        userProfileRepository.updateUserProfile(
            userProfile = userProfile.copy(
                firstName = userProfile.firstName.trim(),
                lastName = userProfile.lastName.trim(),
                telegram = userProfile.telegram?.trim()?.takeIf { it.isNotEmpty() },
                instagram = userProfile.instagram?.trim()?.takeIf { it.isNotEmpty() }
            )
        )
}