package user_profile.domain.repository

import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile(): Result<UserProfile, DataError.Network>
    suspend fun updateUserProfile(userProfile: UserProfile): Result<Unit, DataError>
}