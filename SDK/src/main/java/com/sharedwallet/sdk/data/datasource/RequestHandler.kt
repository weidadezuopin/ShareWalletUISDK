package com.sharedwallet.sdk.data.datasource

import com.sharedwallet.sdk.domain.state.RequestState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

internal class RequestHandler(
    private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun <T> handle(
        block: suspend CoroutineScope.() -> T,
    ): RequestState<T> = withContext(ioDispatcher) {
        try {
            RequestState.Success(block())
        } catch (e: Exception) {
            RequestState.Error(e)
        }
    }
}
