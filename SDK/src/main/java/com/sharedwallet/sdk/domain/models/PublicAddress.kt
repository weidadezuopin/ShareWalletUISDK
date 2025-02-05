package com.sharedwallet.sdk.domain.models

import com.sharedwallet.sdk.domain.enums.CurrencyType

data class PublicAddress(
    val name: String,
    val currencyType: CurrencyType,
    val address: String,
    val contractAddress: String,
)
