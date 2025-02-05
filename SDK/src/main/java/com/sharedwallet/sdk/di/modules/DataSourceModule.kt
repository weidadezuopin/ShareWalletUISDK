package com.sharedwallet.sdk.di.modules

import com.sharedwallet.sdk.data.datasource.DataStoreDataSource
import com.sharedwallet.sdk.data.datasource.JsonProvider
import com.sharedwallet.sdk.data.datasource.RequestHandler
import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val dataSourceModule = module {
    factory { RequestHandler(get(qualifier = named(Dispatcher.IO))) }
    singleOf(::JsonProvider)
    singleOf(::WalletSdkDataSource)
    singleOf(::DataStoreDataSource)
}
