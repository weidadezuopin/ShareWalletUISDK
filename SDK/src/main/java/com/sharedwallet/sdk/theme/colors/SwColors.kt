package com.sharedwallet.sdk.theme.colors

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class SwColors(
    val primary: Color = CyanA400,
    val primaryLight: Color = CyanLight,
    val secondary: Color = Cyan,

    val succeed: Color = SucceedColor,
    val failed: Color = FailedColor,
    val pending: Color = PendingColor,

    val ethPrimary: Color = EthPrimaryColor,
    val ethSecondary: Color = EthSecondaryColor,
    val btcPrimary: Color = BtcPrimaryColor,
    val btcSecondary: Color = BtcSecondaryColor,
    val usdtTrc20Primary: Color = UsdtTrc20PrimaryColor,
    val usdtTrc20Secondary: Color = UsdtTrc20SecondaryColor,
    val usdtErc20Primary: Color = UsdtErc20PrimaryColor,
    val usdtErc20Secondary: Color = UsdtErc20SecondaryColor,
    val trxPrimary: Color = TrxPrimaryColor,
    val trxSecondary: Color = TrxSecondaryColor,

    val selectedCard: Color = LightGreenColor,
    val blueCard: Color = LightBlue20,
    val clickableText: Color = Blue,

    val grayButton: Color = Gray200,
    val grayCard: Color = Gray110,
    val placeholder: Color = Gray350,
    val grayCurrency: Color = Gray390,
)
