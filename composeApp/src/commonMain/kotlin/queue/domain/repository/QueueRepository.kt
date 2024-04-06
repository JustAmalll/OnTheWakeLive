package queue.domain.repository

import com.benasher44.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import queue.domain.model.Line
import queue.domain.model.QueueItem
import queue.domain.model.QueueSocketResponse
import queue.domain.model.ReorderedQueueItem
import user_profile.domain.model.UserProfile

interface QueueRepository {
    suspend fun initSession(): Result<Unit>
    suspend fun getQueue(): Result<List<QueueItem>>
    fun observeQueue(): Flow<QueueSocketResponse>

    suspend fun adminAddUserToTheQueue(userId: Uuid?, line: Line, fullName: String): Result<Unit>
    suspend fun joinTheQueue(line: Line): Result<Unit>
    suspend fun leaveTheQueue(queueItemId: Uuid): Result<Unit>
    suspend fun reorderQueue(reorderedQueueItems: List<ReorderedQueueItem>): Result<Unit>

    suspend fun closeSession()
}