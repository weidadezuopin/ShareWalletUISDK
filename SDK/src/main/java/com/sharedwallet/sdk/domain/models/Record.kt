package com.sharedwallet.sdk.domain.models

import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.RecordState

data class Record(
    val id: String,
    val transactionHash: String,
    val state: RecordState,
    val isSent: Boolean,
    val currency: CurrencyType,
    val amount: String,
    val otherAddress: String,
    val date: String,
)
