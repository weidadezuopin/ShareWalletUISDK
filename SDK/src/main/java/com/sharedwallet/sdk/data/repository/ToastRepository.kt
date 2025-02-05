package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.domain.utils.throttleFirst
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

internal class ToastRepository {

    private val toastChannel = Channel<String>()
    val toastFlow = toastChannel.receiveAsFlow().throttleFirst(1_500)

    fun tryToast(message: String) {
        toastChannel.trySend(message)
    }
}
