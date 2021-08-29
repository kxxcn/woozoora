package dev.kxxcn.woozoora.data

import dev.kxxcn.woozoora.common.base.BaseException
import dev.kxxcn.woozoora.data.source.api.UserNotFoundException

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val exception: Exception) : Result<Nothing>() {

        val messageRes: Int?
            get() = (exception as? BaseException)?.res
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[ data = $data ]"
            is Error -> "Error[ exception = $exception ]"
        }
    }
}

val Result<*>.succeeded
    get() = this is Result.Success && this.data != null

val Result<*>.failed
    get() = this is Result.Error

val Result<*>.userNotFound
    get() = this is Result.Error && this.exception is UserNotFoundException

inline fun <T> Result<T>.ifSucceeded(block: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        block(this.data)
    }
    return this
}

inline fun <T> Result<T>.ifFailed(block: (Result.Error) -> Unit): Result<T> {
    if (failed) {
        block(this as Result.Error)
    }
    return this
}

inline fun <T, R> Result<T>.map(block: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(block(data))
    is Result.Error -> this
}

inline fun <T, R> Result<T>.flatMap(block: (T) -> Result<R>): Result<R> = when (this) {
    is Result.Success -> block(data)
    is Result.Error -> this
}
