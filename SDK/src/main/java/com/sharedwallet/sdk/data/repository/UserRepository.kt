package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import com.sharedwallet.sdk.data.mappers.toSeedWord
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.PrivateKeyFormat
import com.sharedwallet.sdk.domain.models.SeedWord
import com.sharedwallet.sdk.domain.state.RequestState
import com.sharedwallet.sdk.domain.state.UserState

internal class UserRepository(
    private val walletSdkDataSource: WalletSdkDataSource,
) {

    suspend fun getUserState(): UserState {
        val request = walletSdkDataSource.getUserStatus()
        return if (request is RequestState.Success) {
            when (request.data.toInt()) {
                UserState.AccountGenerated.code -> UserState.AccountGenerated
                else -> UserState.None
            }
        } else {
            UserState.None
        }
    }

    suspend fun getSeedPhrase(passcode: String): RequestState<List<SeedWord>> {
        return when (val request = walletSdkDataSource.getSeedPhrase(passcode)) {
            is RequestState.Success -> RequestState.Success(
                request.data.split(' ').map { it.toSeedWord() }
            )
            is RequestState.Error -> RequestState.Error(request.e)
        }
    }

    suspend fun getPublicAddress(currencyType: CurrencyType): String {
        return when (val request = walletSdkDataSource.getPublicAddress(currencyType.keyType)) {
            is RequestState.Success -> {
                if (request.data.isNotEmpty()) {
                    request.data.first().address
                } else {
                    ""
                }
            }
            is RequestState.Error -> {
                request.e.printStackTrace()
                ""
            }
        }
    }

    suspend fun getPrivateKey(
        passcode: String,
        currencyType: CurrencyType,
        format: PrivateKeyFormat
    ): String {
        return when (val request =
            walletSdkDataSource.getUserPrivateKey(passcode, currencyType.keyType, format.key)) {
            is RequestState.Success -> {
                request.data
            }
            is RequestState.Error -> {
                request.e.printStackTrace()
                ""
            }
        }
    }

    suspend fun getPublicAddressForUser(currencyType: CurrencyType, userId: String) =
        walletSdkDataSource.fetchFriendAddress(currencyType.keyType, userId)
}
