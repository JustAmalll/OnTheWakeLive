package queue.data.source.remote

import kotlinx.coroutines.flow.Flow
import queue.domain.module.Line
import queue.domain.module.QueueItem
import queue.domain.module.QueueSocketResponse
import user_profile.domain.model.UserProfile

interface QueueRemoteDataSource {
    suspend fun initSession(): Result<Unit>
    fun observeQueue(): Flow<QueueSocketResponse>

    suspend fun adminAddUserToTheQueue(userId: String?, line: Line, fullName: String): Result<Unit>
    suspend fun joinTheQueue(line: Line): Result<Unit>
    suspend fun leaveTheQueue(queueItemId: String): Result<Unit>

    suspend fun getQueue(): Result<List<QueueItem>>
    suspend fun getQueueItemDetails(queueItemId: String): Result<UserProfile>
    suspend fun adminSearchUsers(searchQuery: String): Result<List<UserProfile>>

    suspend fun closeSession()
}