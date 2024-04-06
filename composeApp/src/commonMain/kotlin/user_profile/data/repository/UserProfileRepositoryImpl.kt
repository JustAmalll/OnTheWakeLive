package user_profile.data.repository

import com.benasher44.uuid.Uuid
import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.onSuccess
import user_profile.data.source.cache.UserProfileCacheDataSource
import user_profile.data.source.remote.UserProfileRemoteDataSource
import user_profile.domain.model.UserProfile
import user_profile.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(
    private val userProfileCacheDataSource: UserProfileCacheDataSource,
    private val userProfileRemoteDataSource: UserProfileRemoteDataSource
) : UserProfileRepository {

    override suspend fun getUserProfile(): Result<UserProfile, DataError.Network> {
        val cachedResult = userProfileCacheDataSource.getUserProfile()

        if (cachedResult is Result.Success && cachedResult.data != null) {
            return Result.Success(cachedResult.data)
        }

        return userProfileRemoteDataSource.getUserProfile().onSuccess {
            userProfileCacheDataSource.cacheUserProfile(userProfile = it)
        }
    }

    override suspend fun updateUserProfile(userProfile: UserProfile): Result<Unit, DataError> =
        userProfileRemoteDataSource.updateUserProfile(userProfile = userProfile).onSuccess {
            return userProfileCacheDataSource.cacheUserProfile(userProfile = userProfile)
        }

    override suspend fun getQueueItemDetails(userId: Uuid): Result<UserProfile, DataError.Network> =
        userProfileRemoteDataSource.getQueueItemDetails(userId = userId)

    override suspend fun searchUsers(
        searchQuery: String
    ): Result<List<UserProfile>, DataError.Network> = userProfileRemoteDataSource.searchUsers(
        searchQuery = searchQuery
    )
}