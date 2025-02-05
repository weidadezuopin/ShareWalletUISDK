package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import com.sharedwallet.sdk.domain.models.SeedWord
import com.sharedwallet.sdk.domain.state.RequestState

internal class RecoverPhraseRepository(
    private val walletSdkDataSource: WalletSdkDataSource,
) {

    suspend fun fetchSeedWord(word: String) =
        when(val request = walletSdkDataSource.fetchSeedWord(word)) {
            is RequestState.Success -> request.data
            is RequestState.Error -> emptyList()
        }

    suspend fun recoverUserAccount(passcode: String, seedPhrase: List<SeedWord>) =
        walletSdkDataSource.recoverUserAccount(
            passcode = passcode,
            seedPhrase = seedPhrase.joinToString(separator = " ") { it.word },
        )
}
