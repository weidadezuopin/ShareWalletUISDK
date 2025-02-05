package com.sharedwallet.sdk.di

import android.content.Context
import com.sharedwallet.sdk.di.modules.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.dsl.koinApplication

internal fun swKoin(applicationContext: Context) = koinApplication {
    androidLogger()
    androidContext(applicationContext)
    modules(
        dataStoreModule,
        dispatcherModule,
        jsonModule,
        dataSourceModule,
        repositoryModule,
        viewModelModule,
    )
}
