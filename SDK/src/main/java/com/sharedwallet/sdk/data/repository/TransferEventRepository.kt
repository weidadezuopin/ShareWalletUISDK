package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.domain.models.TransferEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class TransferEventRepository {

    private val _transferFlow = MutableSharedFlow<TransferEvent>(extraBufferCapacity = 1)
    val transferFlow = _transferFlow.asSharedFlow()

    fun sendEvent(transactionID: TransferEvent) {
        _transferFlow.tryEmit(transactionID)
    }
}
