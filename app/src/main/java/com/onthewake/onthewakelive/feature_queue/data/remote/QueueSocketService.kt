package com.onthewake.onthewakelive.feature_queue.data.remote

import com.onthewake.onthewakelive.feature_queue.domain.module.QueueResponse
import com.onthewake.onthewakelive.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface QueueSocketService {
    suspend fun initSession(firstName: String): Resource<Unit>
    fun observeQueue(): Flow<QueueResponse>
    suspend fun addToQueue(isLeftQueue: Boolean, firstName: String, timestamp: Long)
    suspend fun closeSession()
}