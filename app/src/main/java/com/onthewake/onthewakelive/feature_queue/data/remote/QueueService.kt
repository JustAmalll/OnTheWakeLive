package com.onthewake.onthewakelive.feature_queue.data.remote

import com.onthewake.onthewakelive.feature_profile.domain.module.Profile
import com.onthewake.onthewakelive.feature_queue.data.remote.dto.QueueDto
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import com.onthewake.onthewakelive.util.Constants.BASE_URL
import com.onthewake.onthewakelive.util.Resource

interface QueueService {

    suspend fun getQueue(): List<Queue>
    suspend fun getQueueDetails(queueItemId: String): Resource<Profile>
    suspend fun deleteQueueItem(queueItemId: String): QueueDto?

    sealed class Endpoints(val url: String) {
        object GetQueue : Endpoints("$BASE_URL/queue")
        object DeleteQueueItem : Endpoints("$BASE_URL/queue/item/delete")
    }

}