package user_profile.data.repository

import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.getOrNull
import core.domain.utils.onFailure
import core.domain.utils.onSuccess
import user_profile.data.source.cache.UserProfileCacheDataSource
import user_profile.data.source.remote.UserProfileRemoteDataSource
import user_profile.domain.model.UserProfile
import user_profile.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(
    private val userProfileCacheDataSource: UserProfileCacheDataSource,
    private val userProfileRemoteDataSource: UserProfileRemoteDataSource
): UserProfileRepository {

    override suspend fun getUserProfile(): Result<UserProfile, DataError.Network> {
        val cachedResult = userProfileCacheDataSource.getUserProfile()

        if (cachedResult is Result.Success && cachedResult.data != null) {
            return Result.Success(cachedResult.data)
        }

        return userProfileRemoteDataSource.getUserProfile().onSuccess {
            userProfileCacheDataSource.cacheUserProfile(userProfile = it)
        }
    }
}