package com.sharedwallet.sdk.di.modules

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import org.koin.dsl.module

internal val dataStoreModule = module {
    single {
        val context: Context = get()
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("shared_wallet_data_store") },
        )
    }
}
