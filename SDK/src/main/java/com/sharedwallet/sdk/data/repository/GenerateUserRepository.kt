package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import com.sharedwallet.sdk.data.mappers.toSeedWord
import com.sharedwallet.sdk.domain.models.SeedWord
import com.sharedwallet.sdk.domain.state.RequestState

internal class GenerateUserRepository(
    private val walletSdkDataSource: WalletSdkDataSource,
) {

    suspend fun generateUserAccount(passcode: String): RequestState<List<SeedWord>> {
        return when(val requestState = walletSdkDataSource.generateUserAccount(passcode)) {
            is RequestState.Error -> requestState
            is RequestState.Success -> RequestState.Success(requestState.data.map { it.toSeedWord() })
        }
    }
}
