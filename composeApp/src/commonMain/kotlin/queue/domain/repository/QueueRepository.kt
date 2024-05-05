package queue.domain.repository

import com.benasher44.uuid.Uuid
import core.domain.utils.DataError
import core.domain.utils.Result
import queue.domain.model.Line
import queue.domain.model.QueueItem
import queue.domain.model.ReorderedQueueItem

interface QueueRepository {
    val isSessionActive: Boolean

    suspend fun initSession(): Result<Unit, DataError.Network>
    suspend fun getQueue(): Result<List<QueueItem>, DataError.Network>

    suspend fun observeQueue(
        updateQueue: suspend (List<QueueItem>) -> Unit,
        onError: suspend (DataError.Socket) -> Unit
    )

    suspend fun updateNotificationToken(newToken: String)

    suspend fun adminAddUserToTheQueue(
        userId: Uuid?,
        line: Line,
        fullName: String
    ): Result<Unit, DataError.Socket>

    suspend fun joinTheQueue(
        userId: Uuid,
        line: Line,
        notificationToken: String?
    ): Result<Unit, DataError.Socket>

    suspend fun leaveTheQueue(queueItemId: Uuid): Result<Unit, DataError.Socket>

    suspend fun reorderQueue(
        reorderedQueueItems: List<ReorderedQueueItem>
    ): Result<Unit, DataError.Socket>

    suspend fun closeSession()
}