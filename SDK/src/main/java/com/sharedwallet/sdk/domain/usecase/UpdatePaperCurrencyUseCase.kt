package com.sharedwallet.sdk.domain.usecase

import com.sharedwallet.sdk.data.repository.PaperCurrencyRepository
import com.sharedwallet.sdk.di.WalletKoinComponent
import com.sharedwallet.sdk.domain.enums.PaperCurrency
import org.koin.core.component.inject

internal class UpdatePaperCurrencyUseCase(
    private val paperCurrency: PaperCurrency,
): WalletKoinComponent {

    private val paperCurrencyRepository: PaperCurrencyRepository by inject()

    suspend operator fun invoke() {
        paperCurrencyRepository.setCurrency(paperCurrency)
    }
}
