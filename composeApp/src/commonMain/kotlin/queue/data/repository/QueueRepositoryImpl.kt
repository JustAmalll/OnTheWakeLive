package queue.data.repository


import core.domain.utils.DataError
import core.domain.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import queue.data.source.remote.QueueRemoteDataSource
import queue.domain.model.Line
import queue.domain.model.QueueItem
import queue.domain.model.ReorderedQueueItem
import queue.domain.repository.QueueRepository

class QueueRepositoryImpl(
    private val queueRemoteDataSource: QueueRemoteDataSource
) : QueueRepository {

    override val isSessionActive: Boolean
        get() = queueRemoteDataSource.isSessionActive

    override val connectionStatus: MutableStateFlow<Boolean>
        get() = queueRemoteDataSource.connectionStatus

    override suspend fun initSession(): Result<Unit, DataError.Network> =
        queueRemoteDataSource.initSession()

    override suspend fun observeQueue(
        updateQueue: suspend (List<QueueItem>) -> Unit,
        onError: suspend (DataError.Socket) -> Unit
    ) {
        queueRemoteDataSource.observeQueue(
            updateQueue = updateQueue,
            onError = onError
        )
    }

    override suspend fun updateNotificationToken(newToken: String) {
        queueRemoteDataSource.updateNotificationToken(newToken = newToken)
    }

    override suspend fun adminAddUserToTheQueue(
        userId: Int?,
        line: Line,
        fullName: String
    ): Result<Unit, DataError.Socket> = queueRemoteDataSource.adminAddUserToTheQueue(
        userId = userId,
        line = line,
        fullName = fullName
    )

    override suspend fun joinTheQueue(
        userId: Int,
        line: Line,
        notificationToken: String?
    ): Result<Unit, DataError.Socket> = queueRemoteDataSource.joinTheQueue(
        userId = userId,
        line = line,
        notificationToken = notificationToken
    )

    override suspend fun leaveTheQueue(queueItemId: Int): Result<Unit, DataError.Socket> =
        queueRemoteDataSource.leaveTheQueue(queueItemId = queueItemId)

    override suspend fun reorderQueue(
        reorderedQueueItems: List<ReorderedQueueItem>
    ): Result<Unit, DataError.Socket> = queueRemoteDataSource.reorderQueue(
        reorderedQueueItems = reorderedQueueItems
    )

    override suspend fun getQueue(): Result<List<QueueItem>, DataError.Network> =
        queueRemoteDataSource.getQueue()

    override suspend fun closeSession() {
        queueRemoteDataSource.closeSession()
    }
}