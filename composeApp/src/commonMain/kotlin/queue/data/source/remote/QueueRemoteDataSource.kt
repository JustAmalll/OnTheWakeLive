package queue.data.source.remote

import core.domain.utils.DataError
import core.domain.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import queue.domain.model.Line
import queue.domain.model.QueueItem
import queue.domain.model.ReorderedQueueItem

interface QueueRemoteDataSource {
    val isSessionActive: Boolean
    val connectionStatus: MutableStateFlow<Boolean>

    suspend fun initSession(): Result<Unit, DataError.Network>

    suspend fun observeQueue(
        updateQueue: suspend (List<QueueItem>) -> Unit,
        onError: suspend (DataError.Socket) -> Unit
    )

    suspend fun getQueue(): Result<List<QueueItem>, DataError.Network>
    suspend fun updateNotificationToken(newToken: String)

    suspend fun adminAddUserToTheQueue(
        userId: Int?,
        line: Line,
        fullName: String
    ): Result<Unit, DataError.Socket>

    suspend fun joinTheQueue(
        userId: Int,
        line: Line,
        notificationToken: String?
    ): Result<Unit, DataError.Socket>

    suspend fun leaveTheQueue(
        queueItemId: Int
    ): Result<Unit, DataError.Socket>

    suspend fun reorderQueue(
        reorderedQueueItems: List<ReorderedQueueItem>
    ): Result<Unit, DataError.Socket>

    suspend fun closeSession()
}