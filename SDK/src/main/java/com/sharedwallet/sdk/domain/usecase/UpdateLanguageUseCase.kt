package com.sharedwallet.sdk.domain.usecase

import com.sharedwallet.sdk.data.repository.LanguageRepository
import com.sharedwallet.sdk.di.WalletKoinComponent
import org.koin.core.component.inject

internal class UpdateLanguageUseCase(
    private val language: String?,
): WalletKoinComponent {

    private val languageRepository: LanguageRepository by inject()

    suspend operator fun invoke() {
        languageRepository.setLanguage(language)
    }
}
