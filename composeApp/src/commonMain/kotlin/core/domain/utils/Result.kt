package core.domain.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

typealias RootError = Error

sealed interface Result<out D, out E: RootError> {
    data class Success<out D, out E: RootError>(val data: D): Result<D, E>
    data class Error<out D, out E: RootError>(val error: E): Result<D, E>

    val isSuccess: Boolean get() = this !is Error
    val isFailure: Boolean get() = this is Error
}

@OptIn(ExperimentalContracts::class)
inline fun <Data, Error : RootError> Result<Data, Error>.onSuccess(
    action: (value: Data) -> Unit
): Result<Data, Error> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is Result.Success) {
        action(data)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <Data, Error : RootError> Result<Data, Error>.onFailure(
    action: (error: Error) -> Unit
): Result<Data, Error> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is Result.Error) {
        action(error)
    }
    return this
}
