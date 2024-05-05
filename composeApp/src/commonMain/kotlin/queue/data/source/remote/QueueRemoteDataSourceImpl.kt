package queue.data.source.remote

import com.benasher44.uuid.Uuid
import core.domain.utils.DataError
import core.domain.utils.Result
import core.domain.utils.onFailure
import core.domain.utils.runCatchingNetwork
import core.domain.utils.runCatchingSocket
import core.utils.Constants.WS_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import queue.data.model.QueueSocketAction
import queue.data.model.QueueSocketAction.Leave
import queue.data.model.QueueSocketAction.Reorder
import queue.domain.model.Line
import queue.domain.model.QueueItem
import queue.domain.model.ReorderedQueueItem

class QueueRemoteDataSourceImpl(
    private val client: HttpClient
) : QueueRemoteDataSource {

    private var session: WebSocketSession? = null

    override val isSessionActive: Boolean
        get() = session?.isActive == true

    override suspend fun initSession(): Result<Unit, DataError.Network> =
        withContext(Dispatchers.IO) {
            runCatchingNetwork {
                session = client.webSocketSession { url(urlString = WS_BASE_URL) }
            }
        }

    override suspend fun observeQueue(
        updateQueue: suspend (List<QueueItem>) -> Unit,
        onError: suspend (DataError.Socket) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            runCatchingSocket {
                val session = session ?: run {
                    onError(DataError.Socket.SERVER_CONNECTION_LOST)
                    return@withContext
                }
                for (frame in session.incoming) {
                    frame as? Frame.Text ?: continue
                    updateQueue(Json.decodeFromString(frame.readText()))
                }
            }.onFailure { error ->
                onError(error)
            }
        }
    }

    override suspend fun getQueue(): Result<List<QueueItem>, DataError.Network> =
        withContext(Dispatchers.IO) {
            runCatchingNetwork { client.get("/queue").body<List<QueueItem>>() }
        }

    override suspend fun updateNotificationToken(newToken: String) {
        runCatchingNetwork {
            client.post("/update-notification-token") {
                setBody(newToken)
            }
        }
    }

    override suspend fun adminAddUserToTheQueue(
        userId: Uuid?,
        line: Line,
        fullName: String
    ): Result<Unit, DataError.Socket> = withContext(Dispatchers.IO) {
        runCatchingSocket {
            val session = session ?: return@withContext Result.Error(
                error = DataError.Socket.SERVER_CONNECTION_LOST
            )
            session.sendQueueSocketAction(
                action = QueueSocketAction.AdminAddUser(
                    userId = userId,
                    line = line,
                    fullName = fullName
                )
            )
        }
    }

    override suspend fun joinTheQueue(
        userId: Uuid,
        line: Line,
        notificationToken: String?
    ): Result<Unit, DataError.Socket> = withContext(Dispatchers.IO) {
        runCatchingSocket {
            val session = session ?: return@withContext Result.Error(
                error = DataError.Socket.SERVER_CONNECTION_LOST
            )
            session.sendQueueSocketAction(
                action = QueueSocketAction.Join(
                    userId = userId,
                    line = line,
                    notificationToken = notificationToken
                )
            )
        }
    }

    override suspend fun leaveTheQueue(
        queueItemId: Uuid
    ): Result<Unit, DataError.Socket> = withContext(Dispatchers.IO) {
        runCatchingSocket {
            val session = session ?: return@withContext Result.Error(
                error = DataError.Socket.SERVER_CONNECTION_LOST
            )
            session.sendQueueSocketAction(action = Leave(queueItemId = queueItemId))
        }
    }

    override suspend fun reorderQueue(
        reorderedQueueItems: List<ReorderedQueueItem>
    ): Result<Unit, DataError.Socket> = withContext(Dispatchers.IO) {
        runCatchingSocket {
            val session = session ?: return@withContext Result.Error(
                error = DataError.Socket.SERVER_CONNECTION_LOST
            )
            session.sendQueueSocketAction(
                action = Reorder(reorderedQueueItems = reorderedQueueItems)
            )
        }
    }

    private suspend fun WebSocketSession.sendQueueSocketAction(action: QueueSocketAction) {
        send(
            content = Json.encodeToString(
                serializer = QueueSocketAction.serializer(),
                value = action
            )
        )
    }

    override suspend fun closeSession() {
        session?.close()
        session = null
    }
}