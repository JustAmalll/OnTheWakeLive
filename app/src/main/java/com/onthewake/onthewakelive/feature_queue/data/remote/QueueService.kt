package com.onthewake.onthewakelive.feature_queue.data.remote

import com.onthewake.onthewakelive.feature_profile.domain.module.Profile
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import com.onthewake.onthewakelive.util.Resource

interface QueueService {
    suspend fun getQueue(): List<Queue>
    suspend fun getProfileDetails(queueItemId: String): Resource<Profile>
    suspend fun deleteQueueItem(queueItemId: String): Queue?
}