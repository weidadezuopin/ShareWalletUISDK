package com.sharedwallet.sdk.data.mappers

import com.sharedwallet.sdk.data.models.CoinRatioDto
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.PaperCurrency
import com.sharedwallet.sdk.domain.models.CoinRatio

internal fun CoinRatioDto.toCoinRatio(paperCurrency: PaperCurrency) = CoinRatio(
    type = coinType.currencyTypeFromId() ?: CurrencyType.BTC,
    ratio = when(paperCurrency) {
        PaperCurrency.USD -> usd
        PaperCurrency.YUAN -> yuan
        PaperCurrency.EURO -> euro
    }
)
