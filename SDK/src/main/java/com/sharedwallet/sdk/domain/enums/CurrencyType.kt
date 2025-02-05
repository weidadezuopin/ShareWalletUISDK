package com.sharedwallet.sdk.domain.enums

import androidx.annotation.Keep

@Keep
enum class CurrencyType(val shortName: String, val keyType: Int) {
    ETH("ETH", 2),
    TRX("TRX", 4),
    BTC("BTC", 1),
    USDT_TRC20("USDT-TRC20", 5),
    USDT_ERC20("USDT-ERC20", 3),
}
