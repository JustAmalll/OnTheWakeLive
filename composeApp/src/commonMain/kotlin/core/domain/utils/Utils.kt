package core.domain.utils

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import okio.IOException
import org.jetbrains.compose.resources.ExperimentalResourceApi

inline fun <T, R> T.runCatching(block: T.() -> R): Result<R, NetworkError> = try {
    Result.Success(block())
} catch (exception: IOException) {
    Result.Error(error = NetworkError.NO_INTERNET)
} catch (exception: ClientRequestException) {
    when (exception.response.status) {
        HttpStatusCode.Conflict -> Result.Error(error = NetworkError.INCORRECT_DATA)
        HttpStatusCode.RequestTimeout ->  Result.Error(error = NetworkError.REQUEST_TIMEOUT)
        HttpStatusCode.Unauthorized ->  Result.Error(error = NetworkError.UNAUTHORIZED)
        else ->  Result.Error(error = NetworkError.UNKNOWN)
    }
} catch (exception: ServerResponseException) {
    Result.Error(error = NetworkError.SERVER_ERROR)
} catch (exception: Exception) {
    Result.Error(error = NetworkError.UNKNOWN)
}

//@OptIn(ExperimentalResourceApi::class)
//internal inline fun <T, R> T.runCatching(block: T.() -> R): Result<R, NetworkError> = try {
//    Result.Success(block())
//} catch (exception: IOException) {
//    Result.Error(error = Res.string.no_internet_error)
//} catch (exception: ClientRequestException) {
//    when (exception.response.status) {
//        HttpStatusCode.Conflict -> Result.Error(message = Res.string.incorrect_data_error)
//        HttpStatusCode.RequestTimeout -> Result.Error(message = Res.string.request_timeout_error)
//        else -> Result.Error(message = Res.string.unknown_error)
//    }
//} catch (exception: ServerResponseException) {
//    Result.Error(message = Res.string.server_error)
//} catch (exception: Exception) {
//    Result.Error(message = Res.string.unknown_error)
//}