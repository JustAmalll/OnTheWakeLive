package core.domain.utils

import core.domain.utils.DataError.Network.INCORRECT_DATA
import core.domain.utils.DataError.Network.NO_INTERNET
import core.domain.utils.DataError.Network.REQUEST_TIMEOUT
import core.domain.utils.DataError.Network.SERVER_ERROR
import core.domain.utils.DataError.Network.UNAUTHORIZED
import core.domain.utils.DataError.Network.UNKNOWN
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.CancellationException
import okio.IOException

inline fun <T, R> T.runCatchingNetwork(block: T.() -> R): Result<R, DataError.Network> = try {
    Result.Success(block())
} catch (exception: IOException) {
    Result.Error(error = NO_INTERNET)
} catch (exception: ClientRequestException) {
    when (exception.response.status) {
        HttpStatusCode.Conflict -> Result.Error(error = INCORRECT_DATA)
        HttpStatusCode.RequestTimeout -> Result.Error(error = REQUEST_TIMEOUT)
        HttpStatusCode.Unauthorized -> Result.Error(error = UNAUTHORIZED)
        else -> Result.Error(error = UNKNOWN)
    }
} catch (exception: ServerResponseException) {
    Result.Error(error = SERVER_ERROR)
} catch (exception: Exception) {
    Result.Error(error = UNKNOWN)
}

inline fun <T, R> T.runCatchingSocket(block: T.() -> R): Result<R, DataError.Socket> = try {
    Result.Success(block())
} catch (exception: IOException) {
    Result.Error(error = DataError.Socket.INTERNET_CONNECTION_LOST)
} catch (exception: CancellationException) {
    Result.Error(error = DataError.Socket.SERVER_CONNECTION_LOST)
} catch (exception: Exception) {
    Result.Error(error = DataError.Socket.UNKNOWN)
}

inline fun <T, R> T.runCatchingLocal(block: T.() -> R): Result<R, DataError.Local> = try {
    Result.Success(block())
} catch (exception: Exception) {
    Result.Error(error = DataError.Local.UNKNOWN)
}
