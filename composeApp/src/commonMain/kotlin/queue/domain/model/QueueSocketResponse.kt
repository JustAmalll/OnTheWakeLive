package queue.domain.model

import com.benasher44.uuid.Uuid
import core.domain.utils.UuidSerializer
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
    data class Remove(
        @Serializable(with = UuidSerializer::class)
        @SerialName("queueItemId")
        val queueItemId: Uuid
    ) : QueueSocketResponse

    @Serializable
    @SerialName("Update")
    data class Update(@SerialName("queue") val queue: List<QueueItem>) : QueueSocketResponse
}