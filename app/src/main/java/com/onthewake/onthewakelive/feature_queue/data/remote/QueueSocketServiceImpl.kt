package com.onthewake.onthewakelive.feature_queue.data.remote

import android.content.Context
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.feature_queue.domain.module.QueueResponse
import com.onthewake.onthewakelive.util.Constants.WS_BASE_URL
import com.onthewake.onthewakelive.util.Resource
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class QueueSocketServiceImpl(
    private val client: HttpClient,
    private val context: Context
) : QueueSocketService {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(
        firstName: String
    ): Resource<Unit> = try {
        socket = client.webSocketSession {
            url(urlString = "$WS_BASE_URL?firstName=$firstName")
        }
        if (socket?.isActive == true) Resource.Success(Unit)
        else Resource.Error(context.getString(R.string.couldnt_establish_a_connection))
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: context.getString(R.string.unknown_error))
    }

    override fun observeQueue(): Flow<QueueResponse> = flow {

        val queueState = socket!!
            .incoming
            .consumeAsFlow()
            .filterIsInstance<Frame.Text>()
            .mapNotNull {
                val queueDto = Json.decodeFromString<QueueResponse>(it.readText())
                QueueResponse(isDeleteAction = queueDto.isDeleteAction, queue = queueDto.queue)
            }

        emitAll(queueState)
    }

    override suspend fun addToQueue(
        isLeftQueue: Boolean,
        firstName: String,
        timestamp: Long
    ) {
        socket?.outgoing?.send(Frame.Text("$isLeftQueue/$firstName/$timestamp"))
        println("Successfully added to queue!")
    }

    override suspend fun closeSession() {
        socket?.close()
        socket = null
    }
}
