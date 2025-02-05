package com.sharedwallet.sdk.domain.models

import com.sharedwallet.sdk.domain.enums.CurrencyType

data class CurrencyDetails(
    val currencyType: CurrencyType,
    val balance: String = "",
    val bandwidthPoints: String? = null,
    val bandwidthEnergy: String? = null,
)
