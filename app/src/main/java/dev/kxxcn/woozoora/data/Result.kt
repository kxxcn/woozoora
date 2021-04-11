package dev.kxxcn.woozoora.data

import dev.kxxcn.woozoora.common.base.BaseException
import dev.kxxcn.woozoora.data.source.api.UserNotFoundException

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()

    data class Error(val exception: Exception) : Result<Nothing>() {

        val messageRes: Int?
            get() = (exception as? BaseException)?.res
    }

    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[ data = $data ]"
            is Error -> "Error[ exception = $exception ]"
            Loading -> "Loading"
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
