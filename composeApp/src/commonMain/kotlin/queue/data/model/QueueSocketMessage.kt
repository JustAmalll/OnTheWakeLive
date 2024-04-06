package queue.data.model

import com.benasher44.uuid.Uuid
import core.domain.utils.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import queue.domain.model.Line
import queue.domain.model.ReorderedQueueItem

@Serializable
@SerialName("QueueSocketAction")
sealed interface QueueSocketAction {

    @Serializable
    @SerialName("Join")
    data class Join(@SerialName("line") val line: Line) : QueueSocketAction

    @Serializable
    @SerialName("Leave")
    data class Leave(
        @Serializable(with = UuidSerializer::class)
        @SerialName("queueItemId")
        val queueItemId: Uuid
    ) : QueueSocketAction

    @Serializable
    @SerialName("AdminAddUser")
    data class AdminAddUser(
        @SerialName("line") val line: Line,
        @Serializable(with = UuidSerializer::class)
        @SerialName("userId") val userId: Uuid?,
        @SerialName("fullName") val fullName: String
    ) : QueueSocketAction

    @Serializable
    @SerialName("Reorder")
    data class Reorder(
        @SerialName("reorderedQueueItems")
        val reorderedQueueItems: List<ReorderedQueueItem>
    ) : QueueSocketAction
}