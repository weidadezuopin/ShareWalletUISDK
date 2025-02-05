package com.sharedwallet.sdk.domain.models

import com.sharedwallet.sdk.domain.enums.CurrencyType

data class Currency(
    val type: CurrencyType,
    val balance: String = "0",
    val price: String = "0.00",
)
