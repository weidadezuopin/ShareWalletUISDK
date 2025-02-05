package com.sharedwallet.sdk.di.modules

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val dispatcherModule = module {
    factory(qualifier = named(Dispatcher.Worker)) { Dispatchers.Default }
    factory(qualifier = named(Dispatcher.IO)) { Dispatchers.IO }
    factory(qualifier = named(Dispatcher.Main)) { Dispatchers.Main }
}

internal enum class Dispatcher { Main, IO, Worker }
