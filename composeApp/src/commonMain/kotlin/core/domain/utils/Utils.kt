package core.domain.utils

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import okio.IOException
import core.domain.utils.DataError.Network.*

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

inline fun <T, R> T.runCatchingLocal(block: T.() -> R): Result<R, DataError.Local> = try {
    Result.Success(block())
} catch (exception: Exception) {
    Result.Error(error = DataError.Local.UNKNOWN)
}
