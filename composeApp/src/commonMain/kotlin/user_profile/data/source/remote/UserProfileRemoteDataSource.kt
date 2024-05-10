package user_profile.data.source.remote

import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.model.UpdateUserProfileRequest
import user_profile.domain.model.UserProfile

interface UserProfileRemoteDataSource {
    suspend fun getUserProfile(): Result<UserProfile, DataError.Network>
    suspend fun updateUserProfile(
        updateRequest: UpdateUserProfileRequest,
        photo: ByteArray?
    ): Result<Unit, DataError.Network>

    suspend fun getQueueItemDetails(userId: Int): Result<UserProfile, DataError.Network>
    suspend fun searchUsers(searchQuery: String): Result<List<UserProfile>, DataError.Network>
    suspend fun isUserSubscribed(userId: Int): Result<Boolean, DataError.Network>
    suspend fun activateSubscription(userId: Int): Result<Unit, DataError.Network>
}