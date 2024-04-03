package queue.domain.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueueItem(
    @SerialName("_id") val id: String,
    @SerialName("userId") val userId: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String?,
    @SerialName("photo") val photo: String?,
    @SerialName("line") val line: Line,
    @SerialName("timestamp") val timestamp: Long
)