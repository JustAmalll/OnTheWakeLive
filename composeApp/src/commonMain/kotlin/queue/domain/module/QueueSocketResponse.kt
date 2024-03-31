package queue.domain.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("QueueSocketResponse")
sealed interface QueueSocketResponse {

    @Serializable
    @SerialName("Join")
    data class Join(@SerialName("queueItem") val queueItem: QueueItem) : QueueSocketResponse

    @Serializable
    @SerialName("Remove")
    data class Remove(@SerialName("queueItemId") val queueItemId: String) : QueueSocketResponse
}