package com.onthewake.onthewakelive.feature_queue.data.remote

import com.onthewake.onthewakelive.feature_queue.domain.module.QueueResponse
import com.onthewake.onthewakelive.util.Constants.WS_BASE_URL
import com.onthewake.onthewakelive.util.Resource
import kotlinx.coroutines.flow.Flow

interface QueueSocketService {

    suspend fun initSession (firstName: String): Resource<Unit>
    fun observeQueue(): Flow<QueueResponse>
    suspend fun addToQueue(isLeftQueue: Boolean, firstName: String, timestamp: Long)
    suspend fun closeSession()

    sealed class Endpoints(val url: String) {
        object QueueSocket: Endpoints("$WS_BASE_URL/queue-socket")
    }
}