package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import com.sharedwallet.sdk.data.mappers.currencyTypeFromId
import com.sharedwallet.sdk.data.mappers.toCoinRatio
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.models.CoinRatio
import com.sharedwallet.sdk.domain.models.Transfer
import com.sharedwallet.sdk.domain.state.RequestState
import com.sharedwallet.sdk.domain.utils.TransactionId
import com.sharedwallet.sdk.domain.utils.feeCurrency
import com.sharedwallet.sdk.domain.utils.hasMinerFee

internal class CurrencyRepository(
    private val walletSdkDataSource: WalletSdkDataSource,
    private val paperCurrencyRepository: PaperCurrencyRepository,
) {

    suspend fun getAvailableCurrencies(): List<CurrencyType> {
        return when (val request = walletSdkDataSource.fetchLocalCoins()) {
            is RequestState.Success -> request.data.mapNotNull { it.coinType.currencyTypeFromId() }
            is RequestState.Error -> emptyList()
        }
    }

    suspend fun getCoinRatios(): RequestState<List<CoinRatio>> {
        return when (val request = walletSdkDataSource.getCoinRatio()) {
            is RequestState.Success -> RequestState.Success(request.data.map {
                it.toCoinRatio(paperCurrencyRepository.getCurrency())
            })
            is RequestState.Error -> request
        }
    }

    suspend fun getBalance(currencyType: CurrencyType): RequestState<Double> {
        return when (val request = walletSdkDataSource.getPublicAddress(currencyType.keyType)) {
            is RequestState.Error -> request
            is RequestState.Success -> {
                if (request.data.isNotEmpty()) {
                    walletSdkDataSource.getBalance(currencyType.keyType, request.data.first().address)
                } else {
                    RequestState.Error(IllegalArgumentException(""))
                }
            }
        }
    }

    suspend fun getGasPrices(currencyType: CurrencyType): RequestState<List<Double>> {
        return if (currencyType.hasMinerFee) {
            when(val lowestGasRequest = walletSdkDataSource.getGasPrice(currencyType.keyType)) {
                is RequestState.Success -> RequestState.Success(
                    when (currencyType.feeCurrency) {
                        CurrencyType.ETH -> listOf(
                            lowestGasRequest.data + (ETH_GAS_DIFF * 2),
                            lowestGasRequest.data + ETH_GAS_DIFF,
                            lowestGasRequest.data,
                        )
                        CurrencyType.BTC -> listOf(
                            lowestGasRequest.data + BTC_GAS_DIFF,
                            lowestGasRequest.data,
                        )
                        else -> emptyList()
                    }
                )
                is RequestState.Error -> lowestGasRequest
            }
        } else {
            RequestState.Success(emptyList())
        }
    }

    suspend fun getEstimatedFee(currencyType: CurrencyType, gasPrice: Double) =
        walletSdkDataSource.getTransactionFee(currencyType.keyType, gasPrice)

    suspend fun transfer(
        passcode: String,
        transfer: Transfer,
    ): RequestState<TransactionId> {
        return walletSdkDataSource.transfer(
                currencyType = transfer.currency.keyType,
                publicAddress = transfer.myAddress,
                toAddress = transfer.toAddress,
                passcode = passcode,
                amount = transfer.amount.toDouble(),
                gasPrice = transfer.selectedGasPrice,
            )
    }

    companion object {
        const val ETH_GAS_DIFF = 0.000000002
        const val BTC_GAS_DIFF = 0.000000002
    }
}
