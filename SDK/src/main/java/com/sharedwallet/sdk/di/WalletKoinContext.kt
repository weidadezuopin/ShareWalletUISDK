package com.sharedwallet.sdk.di

import org.koin.core.Koin
import org.koin.core.KoinApplication

internal object WalletKoinContext {
    var koinApp : KoinApplication? = null

    val koin: Koin
        get() = koinApp?.koin ?: error("WalletKoin has not been started")
}
