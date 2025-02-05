package com.sharedwallet.sdk.domain.usecase

import com.sharedwallet.sdk.data.repository.TransactionEventRepository
import com.sharedwallet.sdk.di.WalletKoinComponent
import com.sharedwallet.sdk.domain.utils.TransactionId
import org.koin.core.component.inject

internal class UpdateTransactionUseCase(
    private val transactionID: TransactionId,
): WalletKoinComponent {

    private val transactionEventRepository: TransactionEventRepository by inject()

    operator fun invoke() {
        transactionEventRepository.update(transactionID)
    }
}
