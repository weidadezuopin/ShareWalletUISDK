package com.sharedwallet.sdk.domain.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.theme.SwTheme

val CurrencyType.hasBandwidth: Boolean
    get() = this == CurrencyType.TRX || this == CurrencyType.USDT_TRC20

val CurrencyType.feeCurrency: CurrencyType
    get() = when(this) {
        CurrencyType.USDT_ERC20 -> CurrencyType.ETH
        CurrencyType.USDT_TRC20 -> CurrencyType.TRX
        else -> this
    }

val CurrencyType.doesPayFees: Boolean
    get() = this == feeCurrency

val CurrencyType.hasMinerFee: Boolean
    get() = feeCurrency == CurrencyType.ETH || feeCurrency == CurrencyType.BTC

val CurrencyType.shortNameWithAlis: String
    get () = when(feeCurrency) {
        CurrencyType.ETH -> "${CurrencyType.ETH.shortName} (${CurrencyType.USDT_ERC20.shortName})"
        CurrencyType.TRX -> "${CurrencyType.TRX.shortName} (${CurrencyType.USDT_TRC20.shortName})"
        else -> shortName
    }

val CurrencyType.primaryColor: Color
    @Composable
    get() = when(this) {
        CurrencyType.ETH -> SwTheme.colors.ethPrimary
        CurrencyType.TRX -> SwTheme.colors.trxPrimary
        CurrencyType.BTC -> SwTheme.colors.btcPrimary
        CurrencyType.USDT_TRC20 -> SwTheme.colors.usdtTrc20Primary
        CurrencyType.USDT_ERC20 -> SwTheme.colors.usdtErc20Primary
    }

val CurrencyType.secondaryColor: Color
    @Composable
    get() = when(this) {
        CurrencyType.ETH -> SwTheme.colors.ethSecondary
        CurrencyType.TRX -> SwTheme.colors.trxSecondary
        CurrencyType.BTC -> SwTheme.colors.btcSecondary
        CurrencyType.USDT_TRC20 -> SwTheme.colors.usdtTrc20Secondary
        CurrencyType.USDT_ERC20 -> SwTheme.colors.usdtErc20Secondary
    }

val CurrencyType.logoId: Int
    get() = when(this) {
        CurrencyType.ETH -> R.drawable.sw_logo_eth
        CurrencyType.TRX -> R.drawable.sw_logo_trx
        CurrencyType.BTC -> R.drawable.sw_logo_btc
        CurrencyType.USDT_TRC20 -> R.drawable.sw_logo_usdt_trc20
        CurrencyType.USDT_ERC20 -> R.drawable.sw_logo_usdt_erc20
    }

val String.fromShortName: CurrencyType?
    get() = when(this) {
        CurrencyType.ETH.shortName -> CurrencyType.ETH
        CurrencyType.TRX.shortName -> CurrencyType.TRX
        CurrencyType.BTC.shortName -> CurrencyType.BTC
        CurrencyType.USDT_TRC20.shortName -> CurrencyType.USDT_TRC20
        CurrencyType.USDT_ERC20.shortName -> CurrencyType.USDT_ERC20
        else -> null
    }
