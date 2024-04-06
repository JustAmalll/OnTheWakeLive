package queue.data.source.remote

import com.benasher44.uuid.Uuid
import core.utils.Constants.WS_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import queue.data.model.QueueSocketAction
import queue.data.model.QueueSocketAction.Leave
import queue.data.model.QueueSocketAction.Reorder
import queue.domain.model.Line
import queue.domain.model.QueueItem
import queue.domain.model.QueueSocketResponse
import queue.domain.model.ReorderedQueueItem
import user_profile.domain.model.UserProfile

class QueueRemoteDataSourceImpl(
    private val client: HttpClient
) : QueueRemoteDataSource {

    private var session: WebSocketSession? = null

    override suspend fun initSession(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { session = client.webSocketSession { url(urlString = WS_BASE_URL) } }
    }

    override fun observeQueue(): Flow<QueueSocketResponse> = session!!
        .incoming
        .consumeAsFlow()
        .flowOn(Dispatchers.IO)
        .filterIsInstance<Frame.Text>()
        .mapNotNull {
            Json.decodeFromString(
                deserializer = QueueSocketResponse.serializer(),
                string = it.readText()
            )
        }

    override suspend fun getQueue(): Result<List<QueueItem>> = withContext(Dispatchers.IO) {
        runCatching { client.get("/queue").body<List<QueueItem>>() }
    }

    override suspend fun adminAddUserToTheQueue(
        userId: Uuid?,
        line: Line,
        fullName: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val session = session ?: return@runCatching

            session.sendQueueSocketAction(
                action = QueueSocketAction.AdminAddUser(
                    userId = userId,
                    line = line,
                    fullName = fullName
                )
            )
        }
    }

    override suspend fun joinTheQueue(line: Line): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val session = session ?: return@runCatching
            session.sendQueueSocketAction(action = QueueSocketAction.Join(line = line))
        }
    }

    override suspend fun leaveTheQueue(queueItemId: Uuid): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val session = session ?: return@runCatching
                session.sendQueueSocketAction(action = Leave(queueItemId = queueItemId))
            }
        }

    override suspend fun reorderQueue(
        reorderedQueueItems: List<ReorderedQueueItem>
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val session = session ?: return@runCatching

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
        session = null
    }
}