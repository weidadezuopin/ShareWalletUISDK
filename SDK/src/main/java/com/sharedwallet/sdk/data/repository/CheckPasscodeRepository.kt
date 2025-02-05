package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import com.sharedwallet.sdk.domain.state.RequestState

internal class CheckPasscodeRepository(
    private val walletSdkDataSource: WalletSdkDataSource,
) {

    suspend fun check(passcode: String) =
        (walletSdkDataSource.verifyPasscode(passcode) as? RequestState.Success)?.data == true
}
