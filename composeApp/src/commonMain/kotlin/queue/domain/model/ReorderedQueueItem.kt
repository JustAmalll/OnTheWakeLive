package queue.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReorderedQueueItem(
    @SerialName("queueItemId") val queueItemId: Int,
    @SerialName("newPosition") val newPosition: Long
)