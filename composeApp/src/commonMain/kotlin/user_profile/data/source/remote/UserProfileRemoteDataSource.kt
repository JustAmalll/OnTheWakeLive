package user_profile.data.source.remote

import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.model.UserProfile

interface UserProfileRemoteDataSource {
    suspend fun getUserProfile(): Result<UserProfile, DataError.Network>
    suspend fun updateUserProfile(userProfile: UserProfile): Result<Unit, DataError.Network>
}