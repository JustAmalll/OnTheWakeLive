package com.onthewake.onthewakelive.feature_queue.data.remote.dto

import com.onthewake.onthewakelive.feature_queue.domain.module.QueueItem
import kotlinx.serialization.Serializable

@Serializable
data class QueueDto(
    val id: String,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUri: String,
    val isLeftQueue: Boolean,
    val timestamp: Long
) {
    fun toQueueItem(): QueueItem = QueueItem(
        id = id,
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        profilePictureUri = profilePictureUri,
        isLeftQueue = isLeftQueue,
        timestamp = timestamp
    )
}
