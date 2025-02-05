package com.sharedwallet.sdk.domain.models

import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.utils.TransactionId

data class TransferEvent(
    val transactionId: TransactionId,
    val currencyType: CurrencyType,
    val amount: String,
)
