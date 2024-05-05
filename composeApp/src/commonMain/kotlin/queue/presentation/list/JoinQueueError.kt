package queue.presentation.list

import core.domain.utils.Error
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.already_in_queue_error
import onthewakelive.composeapp.generated.resources.interval_error
import onthewakelive.composeapp.generated.resources.unknown_error
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

enum class JoinQueueError : Error {
    ALREADY_IN_QUEUE_ERROR,
    INTERVAL_ERROR,
    UNKNOWN_ERROR
}

@OptIn(ExperimentalResourceApi::class)
suspend fun JoinQueueError.asString(): String = when (this) {
    JoinQueueError.ALREADY_IN_QUEUE_ERROR -> getString(resource = Res.string.already_in_queue_error)
    JoinQueueError.INTERVAL_ERROR -> getString(resource = Res.string.interval_error)
    JoinQueueError.UNKNOWN_ERROR -> getString(resource = Res.string.unknown_error)
}