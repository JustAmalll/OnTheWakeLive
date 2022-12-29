package com.onthewake.onthewakelive.feature_queue.data.remote

import com.onthewake.onthewakelive.feature_profile.domain.module.Profile
import com.onthewake.onthewakelive.feature_queue.domain.module.QueueItem
import com.onthewake.onthewakelive.core.util.Resource

interface QueueService {
    suspend fun getQueue(): List<QueueItem>
    suspend fun getProfileDetails(queueItemId: String): Resource<Profile>
    suspend fun deleteQueueItem(queueItemId: String): Resource<QueueItem>
}