package com.sharedwallet.sdk.data.mappers

import com.sharedwallet.sdk.domain.enums.CurrencyType

fun Int.currencyTypeFromId() = CurrencyType.values().firstOrNull { it.keyType == this }

fun String.currencyTypeFromName() = CurrencyType.values().firstOrNull { it.shortName == this }
