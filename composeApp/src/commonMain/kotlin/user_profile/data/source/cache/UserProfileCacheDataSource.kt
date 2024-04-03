package user_profile.data.source.cache

import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.model.UserProfile

interface UserProfileCacheDataSource {
    suspend fun cacheUserProfile(userProfile: UserProfile): Result<Unit, DataError.Local>
    suspend fun getUserProfile(): Result<UserProfile?, DataError.Local>
}