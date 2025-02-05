package com.sharedwallet.sdk.domain.models

import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.RecordState

data class Transaction(
    val transactionId: String,
    val toId: String,
    val fromId: String,
    val otherName: String? = null,
    val currency: CurrencyType,
    val state: RecordState,
    val isSent: Boolean,
    val amount: String,
    val confirmDate: String? = null,
    val networkFee: String? = null,
    val gasPrice: String? = null,
    val gasLimit: String? = null,
    val gasUsed: String? = null,
    val confirmations: String? = null,
)
