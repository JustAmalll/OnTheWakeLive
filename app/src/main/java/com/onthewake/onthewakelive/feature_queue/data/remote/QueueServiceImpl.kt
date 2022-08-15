package com.onthewake.onthewakelive.feature_queue.data.remote

import com.onthewake.onthewakelive.feature_queue.data.remote.dto.QueueDto
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import io.ktor.client.*
import io.ktor.client.request.*

class QueueServiceImpl(
    private val client: HttpClient
) : QueueService {

    override suspend fun getQueue(): List<Queue> =
        try {
            client.get<List<QueueDto>>(
                QueueService.Endpoints.GetQueue.url
            ).map { it.toQueue() }
        } catch (e: Exception) {
            emptyList()
        }

    override suspend fun deleteQueueItem(queueItemId: String): QueueDto? {
        return try {
            client.delete(
                QueueService.Endpoints.DeleteQueueItem.url
            ) {
                parameter("queueItemId", queueItemId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}