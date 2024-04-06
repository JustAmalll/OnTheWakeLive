package queue.domain.model

import com.benasher44.uuid.Uuid
import core.domain.utils.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReorderedQueueItem(
    @Serializable(with = UuidSerializer::class)
    @SerialName("queueItemId")
    val queueItemId: Uuid,
    @SerialName("newPosition") val newPosition: Long
)