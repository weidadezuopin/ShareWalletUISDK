package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.domain.enums.CurrencyType
import java.text.DecimalFormat

internal class CurrencyFormatter {

    private val gasFormatter = DecimalFormat("#.############")
    private val eightDigitsFormatter = DecimalFormat("#.########")
    private val sixDigitsFormatter = DecimalFormat("#.######")
    private val priceFormatter = DecimalFormat("0.00")

    fun formatGas(value: Double): String = gasFormatter.format(value)

    fun formatAmount(value: Double, currencyType: CurrencyType): String = when(currencyType) {
        CurrencyType.BTC,
        CurrencyType.ETH -> eightDigitsFormatter.format(value)
        CurrencyType.TRX,
        CurrencyType.USDT_TRC20,
        CurrencyType.USDT_ERC20 -> sixDigitsFormatter.format(value)
    }

    fun formatPrice(value: Double): String = priceFormatter.format(value)

    fun formatEditableAmount(text: String, currencyType: CurrencyType): String? {
        return if (text.toDoubleOrNull() != null) {
            val numberAfterDot = when(currencyType) {
                CurrencyType.BTC,
                CurrencyType.ETH -> 8
                CurrencyType.TRX,
                CurrencyType.USDT_TRC20,
                CurrencyType.USDT_ERC20 -> 6
            }
            val decimalLength =
                text.substringAfter(delimiter = '.', missingDelimiterValue = "").length
            text.dropLast(if (decimalLength > numberAfterDot) decimalLength - numberAfterDot else 0)
        } else if (text.isEmpty()) {
            ""
        } else {
            null
        }
    }
}
