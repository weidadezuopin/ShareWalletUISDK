package com.sharedwallet.sdk.domain.usecase

import com.sharedwallet.sdk.data.repository.TransferEventRepository
import com.sharedwallet.sdk.di.WalletKoinComponent
import org.koin.core.component.inject

internal class TransferEventUseCase: WalletKoinComponent {

    private val transferEventRepository: TransferEventRepository by inject()

    operator fun invoke() = transferEventRepository.transferFlow
}
