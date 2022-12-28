package com.onthewake.onthewakelive.feature_queue.data.remote

import com.onthewake.onthewakelive.feature_profile.domain.module.Profile
import com.onthewake.onthewakelive.feature_queue.data.remote.dto.QueueDto
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import com.onthewake.onthewakelive.core.util.Constants.BASE_URL
import com.onthewake.onthewakelive.core.util.Resource
import io.ktor.client.*
import io.ktor.client.request.*
import retrofit2.HttpException
import java.io.IOException

class QueueServiceImpl(
    private val client: HttpClient,
    private val queueApi: QueueApi
) : QueueService {

    override suspend fun getQueue(): List<Queue> =
        try {
            client.get<List<QueueDto>>(
                urlString = "$BASE_URL/queue"
            ).map { it.toQueue() }
        } catch (e: Exception) {
            emptyList()
        }

    override suspend fun getProfileDetails(queueItemId: String): Resource<Profile> {
        return try {
            val response = queueApi.getProfileDetails(queueItemId = queueItemId)
            if (response.successful) {
                Resource.Success(response.data?.toProfile())
            } else {
                response.message?.let { msg ->
                    Resource.Error(msg)
                } ?: Resource.Error("Unknown Error")
            }
        } catch (e: IOException) {
            Resource.Error("Oops! Couldn't reach server. Check your internet connection.")
        } catch (e: HttpException) {
            Resource.Error("Oops! Something went wrong. Please try again")
        }
    }

    override suspend fun deleteQueueItem(queueItemId: String): Queue? {
        return try {
            client.delete(urlString = "$BASE_URL/queue/item/delete") {
                parameter("queueItemId", queueItemId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}