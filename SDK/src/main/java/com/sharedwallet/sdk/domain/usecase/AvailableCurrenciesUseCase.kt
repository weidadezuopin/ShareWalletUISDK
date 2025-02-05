package com.sharedwallet.sdk.domain.usecase

import com.sharedwallet.sdk.data.repository.CurrencyRepository
import com.sharedwallet.sdk.data.repository.InitSdkRepository
import com.sharedwallet.sdk.di.WalletKoinComponent
import com.sharedwallet.sdk.domain.enums.CurrencyType
import org.koin.core.component.inject

internal class AvailableCurrenciesUseCase: WalletKoinComponent {

    private val currencyRepository: CurrencyRepository by inject()
    private val initSdkRepository: InitSdkRepository by inject()

    suspend operator fun invoke(): List<CurrencyType> {
        initSdkRepository.syncCoins()
        return currencyRepository.getAvailableCurrencies()
    }
}
