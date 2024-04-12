package core.domain.utils

import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.incorrect_data_error
import onthewakelive.composeapp.generated.resources.no_internet_error
import onthewakelive.composeapp.generated.resources.request_timeout_error
import onthewakelive.composeapp.generated.resources.server_connection_lost
import onthewakelive.composeapp.generated.resources.server_error
import onthewakelive.composeapp.generated.resources.unknown_error
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

sealed interface DataError : Error {
    enum class Network : DataError {
        UNAUTHORIZED,
        REQUEST_TIMEOUT,
        NO_INTERNET,
        SERVER_ERROR,
        INCORRECT_DATA,
        UNKNOWN
    }

    enum class Socket : DataError {
        SERVER_CONNECTION_LOST,
        INTERNET_CONNECTION_LOST,
        UNKNOWN
    }

    enum class Local : DataError {
        UNKNOWN
    }
}

suspend fun DataError.asString(): String = when (this) {
    is DataError.Network -> asString()
    is DataError.Socket -> asString()
    is DataError.Local -> asString()
}

@OptIn(ExperimentalResourceApi::class)
suspend fun DataError.Network.asString(): String = when (this) {
    DataError.Network.UNAUTHORIZED -> getString(resource = Res.string.unknown_error)
    DataError.Network.REQUEST_TIMEOUT -> getString(resource = Res.string.request_timeout_error)
    DataError.Network.NO_INTERNET -> getString(resource = Res.string.no_internet_error)
    DataError.Network.SERVER_ERROR -> getString(resource = Res.string.server_error)
    DataError.Network.INCORRECT_DATA -> getString(resource = Res.string.incorrect_data_error)
    DataError.Network.UNKNOWN -> getString(resource = Res.string.unknown_error)
}

@OptIn(ExperimentalResourceApi::class)
suspend fun DataError.Socket.asString(): String = when (this) {
    DataError.Socket.SERVER_CONNECTION_LOST -> getString(resource = Res.string.server_connection_lost)
    DataError.Socket.INTERNET_CONNECTION_LOST -> getString(resource = Res.string.no_internet_error)
    DataError.Socket.UNKNOWN -> getString(resource = Res.string.unknown_error)
}

@OptIn(ExperimentalResourceApi::class)
suspend fun DataError.Local.asString(): String = when (this) {
    DataError.Local.UNKNOWN -> getString(resource = Res.string.unknown_error)
}