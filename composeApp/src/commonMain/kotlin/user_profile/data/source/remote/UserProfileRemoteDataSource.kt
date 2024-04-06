package user_profile.data.source.remote

import com.benasher44.uuid.Uuid
import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.model.UserProfile

interface UserProfileRemoteDataSource {
    suspend fun getUserProfile(): Result<UserProfile, DataError.Network>
    suspend fun updateUserProfile(userProfile: UserProfile): Result<Unit, DataError.Network>
    suspend fun getQueueItemDetails(userId: Uuid): Result<UserProfile, DataError.Network>
    suspend fun searchUsers(searchQuery: String): Result<List<UserProfile>, DataError.Network>
}