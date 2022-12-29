package com.onthewake.onthewakelive.feature_queue.data.remote

import com.onthewake.onthewakelive.core.util.Constants.BASE_URL
import com.onthewake.onthewakelive.core.util.Resource
import com.onthewake.onthewakelive.feature_profile.domain.module.Profile
import com.onthewake.onthewakelive.feature_queue.data.remote.dto.QueueDto
import com.onthewake.onthewakelive.feature_queue.domain.module.QueueItem
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import retrofit2.HttpException
import java.io.IOException

class QueueServiceImpl(
    private val client: HttpClient,
    private val queueApi: QueueApi
) : QueueService {

    override suspend fun getQueue(): List<QueueItem> =
        try {
            client.get<List<QueueDto>>(
                urlString = "$BASE_URL/queue"
            ).map { it.toQueueItem() }
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
        } catch (e: HttpException) {
            Resource.Error("Oops! Something went wrong. Please try again")
        } catch (e: IOException) {
            Resource.Error("Oops! Couldn't reach server. Check your internet connection.")
        }
    }

    override suspend fun deleteQueueItem(queueItemId: String): Resource<QueueItem> {
        return try {
            val result = client.delete<QueueItem>(urlString = "$BASE_URL/queue/item/delete") {
                parameter("queueItemId", queueItemId)
            }
            Resource.Success(result)
        } catch (e: HttpException) {
            Resource.Error("Oops! Something went wrong. Please try again")
        } catch (e: ClientRequestException) {
            Resource.Error("Oops! Something went wrong. Please try again")
        } catch (e: IOException) {
            Resource.Error("Oops! Couldn't reach server. Check your internet connection.")
        }
    }
}