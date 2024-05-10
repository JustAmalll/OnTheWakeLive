package queue.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import queue.domain.model.Line
import queue.domain.model.ReorderedQueueItem

@Serializable
@SerialName("QueueSocketAction")
sealed interface QueueSocketAction {

    @Serializable
    @SerialName("Join")
    data class Join(
        @SerialName("userId") val userId: Int,
        @SerialName("line") val line: Line,
        @SerialName("notificationToken") val notificationToken: String?
    ) : QueueSocketAction

    @Serializable
    @SerialName("Leave")
    data class Leave(
        @SerialName("queueItemId") val queueItemId: Int
    ) : QueueSocketAction

    @Serializable
    @SerialName("AdminAddUser")
    data class AdminAddUser(
        @SerialName("line") val line: Line,
        @SerialName("userId") val userId: Int?,
        @SerialName("fullName") val fullName: String
    ) : QueueSocketAction

    @Serializable
    @SerialName("Reorder")
    data class Reorder(
        @SerialName("reorderedQueueItems")
        val reorderedQueueItems: List<ReorderedQueueItem>
    ) : QueueSocketAction
}