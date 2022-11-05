package com.onthewake.onthewakelive.feature_queue.data.remote

import com.onthewake.onthewakelive.core.data.dto.response.BasicApiResponse
import com.onthewake.onthewakelive.feature_profile.domain.module.Profile
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import retrofit2.http.GET
import retrofit2.http.Query

interface QueueApi {

    @GET("/queue/item/details")
    suspend fun getQueueDetails(
        @Query("queueItemId") queueItemId: String
    ): BasicApiResponse<Profile>

}