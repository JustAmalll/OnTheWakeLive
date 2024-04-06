package queue.domain.model

import com.benasher44.uuid.Uuid
import core.domain.utils.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueueItem(
    @Serializable(with = UuidSerializer::class)
    @SerialName("id") val id: Uuid,
    @Serializable(with = UuidSerializer::class)
    @SerialName("userId") val userId: Uuid,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String?,
    @SerialName("photo") val photo: String?,
    @SerialName("line") val line: Line,
    @SerialName("position") val position: Long
)