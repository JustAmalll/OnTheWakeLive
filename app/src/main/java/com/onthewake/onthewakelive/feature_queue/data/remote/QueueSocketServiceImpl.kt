package com.onthewake.onthewakelive.feature_queue.data.remote

import android.content.Context
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.feature_queue.domain.module.QueueResponse
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
            url("${QueueSocketService.Endpoints.QueueSocket.url}?firstName=$firstName")
        }
        if (socket?.isActive == true) Resource.Success(Unit)
        else Resource.Error(context.getString(R.string.couldnt_establish_a_connection))
    } catch (e: Exception) {
        e.printStackTrace()
        Resource.Error(e.localizedMessage ?: context.getString(R.string.unknown_error))
    }

    override fun observeQueue(): Flow<QueueResponse> =
        try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val queueDto = Json.decodeFromString<QueueResponse>(json)
                    QueueResponse(isDeleteAction = queueDto.isDeleteAction, queue = queueDto.queue)
                } ?: flow { }
        } catch (e: Exception) {
            e.printStackTrace()
            flow { }
        }

    override suspend fun addToQueue(
        leftQueue: String, firstName: String, timestamp: Long
    ) {
        try {
            socket?.send(Frame.Text("$leftQueue/$firstName/$timestamp"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override suspend fun closeSession() {
        socket?.close()
    }
}
