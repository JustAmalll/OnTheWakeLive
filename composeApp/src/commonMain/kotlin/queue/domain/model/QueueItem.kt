package queue.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueueItem(
    @SerialName("id") val id: Int,
    @SerialName("userId") val userId: Int?,
    @SerialName("firstName") val firstName: String,
    @SerialName("lastName") val lastName: String?,
    @SerialName("photo") val photo: String?,
    @SerialName("line") val line: Line,
    @SerialName("position") val position: Long
)