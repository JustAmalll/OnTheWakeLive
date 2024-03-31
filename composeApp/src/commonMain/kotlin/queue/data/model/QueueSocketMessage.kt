package queue.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import queue.domain.module.Line

@Serializable
@SerialName("QueueSocketAction")
sealed interface QueueSocketAction {

    @Serializable
    @SerialName("Join")
    data class Join(@SerialName("line") val line: Line) : QueueSocketAction

    @Serializable
    @SerialName("Leave")
    data class Leave(@SerialName("queueItemId") val queueItemId: String) : QueueSocketAction

    @Serializable
    @SerialName("AdminAddUser")
    data class AdminAddUser(
        @SerialName("line") val line: Line,
        @SerialName("userId") val userId: String?,
        @SerialName("fullName") val fullName: String
    ) : QueueSocketAction
}