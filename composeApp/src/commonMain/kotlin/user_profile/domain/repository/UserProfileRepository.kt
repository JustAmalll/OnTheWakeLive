package user_profile.domain.repository

import com.benasher44.uuid.Uuid
import core.domain.utils.DataError
import core.domain.utils.Result
import user_profile.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile(): Result<UserProfile, DataError.Network>
    suspend fun updateUserProfile(userProfile: UserProfile): Result<Unit, DataError>
    suspend fun getQueueItemDetails(userId: Uuid): Result<UserProfile, DataError.Network>
    suspend fun searchUsers(searchQuery: String): Result<List<UserProfile>, DataError.Network>
}