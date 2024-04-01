package core.domain.utils

import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.incorrect_data_error
import onthewakelive.composeapp.generated.resources.no_internet_error
import onthewakelive.composeapp.generated.resources.request_timeout_error
import onthewakelive.composeapp.generated.resources.server_error
import onthewakelive.composeapp.generated.resources.unknown_error
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString

enum class NetworkError : Error {
    UNAUTHORIZED,
    REQUEST_TIMEOUT,
    NO_INTERNET,
    SERVER_ERROR,
    INCORRECT_DATA,
    UNKNOWN
}

@OptIn(ExperimentalResourceApi::class)
suspend fun NetworkError.asString(): String = when (this) {
    NetworkError.UNAUTHORIZED -> getString(resource = Res.string.unknown_error)
    NetworkError.REQUEST_TIMEOUT -> getString(resource = Res.string.request_timeout_error)
    NetworkError.NO_INTERNET -> getString(resource = Res.string.no_internet_error)
    NetworkError.SERVER_ERROR -> getString(resource = Res.string.server_error)
    NetworkError.INCORRECT_DATA -> getString(resource = Res.string.incorrect_data_error)
    NetworkError.UNKNOWN -> getString(resource = Res.string.unknown_error)
}