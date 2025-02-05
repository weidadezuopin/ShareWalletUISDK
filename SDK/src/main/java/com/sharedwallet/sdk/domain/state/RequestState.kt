package com.sharedwallet.sdk.domain.state

sealed class RequestState<out T> {
    class Success<T>(val data: T): RequestState<T>()
    class Error(val e: Exception): RequestState<Nothing>()
}
