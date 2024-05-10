package user_profile.data.repository


import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.onSuccess
import user_profile.data.source.cache.UserProfileCacheDataSource
import user_profile.data.source.remote.UserProfileRemoteDataSource
import user_profile.domain.model.UpdateUserProfileRequest
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

    override suspend fun updateUserProfile(
        updateRequest: UpdateUserProfileRequest,
        newPhotoBytes: ByteArray?
    ): Result<Unit, DataError.Network> = userProfileRemoteDataSource.updateUserProfile(
        updateRequest = updateRequest, photo = newPhotoBytes
    ).onSuccess {
        userProfileCacheDataSource.clearCachedUserProfile()
    }

    override suspend fun getQueueItemDetails(userId: Int): Result<UserProfile, DataError.Network> =
        userProfileRemoteDataSource.getQueueItemDetails(userId = userId)

    override suspend fun searchUsers(
        searchQuery: String
    ): Result<List<UserProfile>, DataError.Network> = userProfileRemoteDataSource.searchUsers(
        searchQuery = searchQuery
    )

    override suspend fun isUserSubscribed(userId: Int): Result<Boolean, DataError.Network> =
        userProfileRemoteDataSource.isUserSubscribed(userId = userId)

    override suspend fun activateUserSubscription(userId: Int): Result<Unit, DataError.Network> =
        userProfileRemoteDataSource.activateSubscription(userId = userId)
}