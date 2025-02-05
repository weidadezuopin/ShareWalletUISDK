package com.sharedwallet.sdk.di.modules

import com.sharedwallet.sdk.data.repository.*
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val repositoryModule = module {
    factoryOf(::InitSdkRepository)
    factoryOf(::LanguageRepository)
    factoryOf(::TutorialRepository)
    singleOf(::ToastRepository)
    factoryOf(::UserRepository)
    factoryOf(::GenerateUserRepository)
    factoryOf(::RecoverPhraseRepository)
    factoryOf(::PaperCurrencyRepository)
    factoryOf(::CurrencyRepository)
    factoryOf(::CheckPasscodeRepository)
    factoryOf(::LocalAddressRepository)
    factory { QrCodeRepository(get(qualifier = named(Dispatcher.Worker))) }
    factoryOf(::CurrencyFormatter)
    factoryOf(::DateFormatter)
    factoryOf(::RecentRecordsRepository)
    factoryOf(::RecordsRepository)
    factoryOf(::TransactionRepository)

    singleOf(::TransactionEventRepository)
    singleOf(::TransferEventRepository)
}
