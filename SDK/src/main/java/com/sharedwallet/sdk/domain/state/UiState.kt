package com.sharedwallet.sdk.domain.state

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    class Success<T>(val data: T): UiState<T>()
    class Error(val e: Exception): UiState<Nothing>()
}
