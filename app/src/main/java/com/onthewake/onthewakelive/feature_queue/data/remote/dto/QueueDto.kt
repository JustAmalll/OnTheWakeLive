package com.onthewake.onthewakelive.feature_queue.data.remote.dto

import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import kotlinx.serialization.Serializable

@Serializable
data class QueueDto(
    val id: String,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val profilePictureFileName: String,
    val leftQueue: String,
    val timestamp: Long
) {
    fun toQueue(): Queue = Queue(
        id = id,
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        profilePictureFileName = profilePictureFileName,
        leftQueue = leftQueue,
        timestamp = timestamp
    )
}
