package com.sharedwallet.sdk.domain.usecase

import com.sharedwallet.sdk.data.repository.InitSdkRepository
import com.sharedwallet.sdk.di.WalletKoinComponent
import org.koin.core.component.inject

internal class InitSdkUseCase(
    private val clientId: String,
    private val apiLink: String,
): WalletKoinComponent {

    private val initSdkRepository: InitSdkRepository by inject()

    suspend operator fun invoke() {
        initSdkRepository.init(
            clientId = clientId,
            apiLink = apiLink,
        )
    }
}
