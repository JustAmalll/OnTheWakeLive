package user_profile.domain.use_case

import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.model.UpdateUserProfileRequest
import user_profile.domain.repository.UserProfileRepository

class EditProfileUseCase(
    private val userProfileRepository: UserProfileRepository
) {

    suspend operator fun invoke(
        updateRequest: UpdateUserProfileRequest,
        newPhotoBytes: ByteArray?
    ): Result<Unit, DataError> = userProfileRepository.updateUserProfile(
        updateRequest = updateRequest.copy(
            firstName = updateRequest.firstName.trim(),
            lastName = updateRequest.lastName.trim(),
            telegram = updateRequest.telegram?.trim()?.takeIf { it.isNotEmpty() },
            instagram = updateRequest.instagram?.trim()?.takeIf { it.isNotEmpty() }
        ),
        newPhotoBytes = newPhotoBytes
    )
}