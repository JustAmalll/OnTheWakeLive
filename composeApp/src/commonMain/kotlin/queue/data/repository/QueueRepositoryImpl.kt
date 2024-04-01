package queue.data.repository

import kotlinx.coroutines.flow.Flow
import queue.data.source.remote.QueueRemoteDataSource
import queue.domain.module.Line
import queue.domain.module.QueueItem
import queue.domain.module.QueueSocketResponse
import queue.domain.repository.QueueRepository
import user_profile.domain.model.UserProfile

class QueueRepositoryImpl(
    private val queueRemoteDataSource: QueueRemoteDataSource
) : QueueRepository {

    override suspend fun initSession(): Result<Unit> =
        queueRemoteDataSource.initSession()

    override fun observeQueue(): Flow<QueueSocketResponse> =
        queueRemoteDataSource.observeQueue()

    override suspend fun adminAddUserToTheQueue(
        userId: String?,
        line: Line,
        fullName: String
    ): Result<Unit> = queueRemoteDataSource.adminAddUserToTheQueue(
        userId = userId,
        line = line,
        fullName = fullName
    )

    override suspend fun joinTheQueue(line: Line): Result<Unit> =
        queueRemoteDataSource.joinTheQueue(line = line)

    override suspend fun leaveTheQueue(queueItemId: String): Result<Unit> =
        queueRemoteDataSource.leaveTheQueue(queueItemId = queueItemId)

    override suspend fun getQueue(): Result<List<QueueItem>> =
        queueRemoteDataSource.getQueue()

    override suspend fun getQueueItemDetails(queueItemId: String): Result<UserProfile> =
        queueRemoteDataSource.getQueueItemDetails(queueItemId = queueItemId)

    override suspend fun adminSearchUsers(searchQuery: String): Result<List<UserProfile>> =
        queueRemoteDataSource.adminSearchUsers(searchQuery = searchQuery)

    override suspend fun closeSession() {
        queueRemoteDataSource.closeSession()
    }
}