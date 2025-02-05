package com.sharedwallet.sdk.di

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

internal interface WalletKoinComponent: KoinComponent {

    override fun getKoin(): Koin = WalletKoinContext.koin
}
