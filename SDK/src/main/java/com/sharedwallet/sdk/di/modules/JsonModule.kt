package com.sharedwallet.sdk.di.modules

import kotlinx.serialization.json.Json
import org.koin.dsl.module

internal val jsonModule = module {
    factory {
        Json {
            encodeDefaults = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }
}
