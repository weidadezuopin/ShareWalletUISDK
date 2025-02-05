package com.sharedwallet.sdk.domain.models

import com.sharedwallet.sdk.domain.enums.CurrencyType

data class CoinRatio(
    val type: CurrencyType,
    val ratio: Double,
)
