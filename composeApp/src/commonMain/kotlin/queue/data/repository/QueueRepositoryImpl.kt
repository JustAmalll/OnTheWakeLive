package queue.data.repository

import com.benasher44.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import queue.data.source.remote.QueueRemoteDataSource
import queue.domain.model.Line
import queue.domain.model.QueueItem
import queue.domain.model.QueueSocketResponse
import queue.domain.model.ReorderedQueueItem
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
        userId: Uuid?,
        line: Line,
        fullName: String
    ): Result<Unit> = queueRemoteDataSource.adminAddUserToTheQueue(
        userId = userId,
        line = line,
        fullName = fullName
    )

    override suspend fun joinTheQueue(line: Line): Result<Unit> =
        queueRemoteDataSource.joinTheQueue(line = line)

    override suspend fun leaveTheQueue(queueItemId: Uuid): Result<Unit> =
        queueRemoteDataSource.leaveTheQueue(queueItemId = queueItemId)

    override suspend fun reorderQueue(reorderedQueueItems: List<ReorderedQueueItem>): Result<Unit> =
        queueRemoteDataSource.reorderQueue(reorderedQueueItems = reorderedQueueItems)

    override suspend fun getQueue(): Result<List<QueueItem>> =
        queueRemoteDataSource.getQueue()

    override suspend fun closeSession() {
        queueRemoteDataSource.closeSession()
    }
}