package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import com.sharedwallet.sdk.data.mappers.toTransaction
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.models.Transaction
import com.sharedwallet.sdk.domain.state.RequestState

internal class TransactionRepository(
    private val walletSdkDataSource: WalletSdkDataSource,
    private val currencyFormatter: CurrencyFormatter,
    private val dateFormatter: DateFormatter,
) {

    suspend fun getTransaction(
        currencyType: CurrencyType,
        transactionHash: String,
    ): RequestState<Transaction> {
        return when (val request = walletSdkDataSource.getPublicAddress(currencyType.keyType)) {
            is RequestState.Error -> request
            is RequestState.Success -> {
                if (request.data.isNotEmpty()) {
                    when (val request1 =
                        walletSdkDataSource.getTransaction(
                            currencyType.keyType,
                            request.data.first().address,
                            transactionHash
                        )) {
                        is RequestState.Success -> {
                            RequestState.Success(
                                request1.data.toTransaction(
                                    currencyType = currencyType,
                                    formatter = currencyFormatter,
                                    dateFormatter = dateFormatter,
                                )
                            )
                        }
                        is RequestState.Error -> request1
                    }
                } else {
                    RequestState.Error(IllegalArgumentException(""))
                }
            }
        }
    }
}
