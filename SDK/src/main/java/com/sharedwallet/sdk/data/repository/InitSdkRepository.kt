package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource

internal class InitSdkRepository(
    private val walletSdkDataSource: WalletSdkDataSource,
) {

    suspend fun init(clientId: String, apiLink: String) =
        walletSdkDataSource.init(clientId, apiLink)

    suspend fun syncCoins() {
        walletSdkDataSource.getCoinStatuses()
    }
}
