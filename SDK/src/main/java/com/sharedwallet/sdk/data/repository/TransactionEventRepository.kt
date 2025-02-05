package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.domain.utils.TransactionId
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter

internal class TransactionEventRepository {

    private val _updateFlow = MutableSharedFlow<TransactionId>(extraBufferCapacity = 10)

    fun forTransaction(transactionID: TransactionId) = _updateFlow
        .filter { it == transactionID }

    fun update(transactionID: TransactionId) {
        _updateFlow.tryEmit(transactionID)
    }
}
