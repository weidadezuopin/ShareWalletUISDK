package com.sharedwallet.sdk.di.modules

import com.sharedwallet.sdk.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::SetPasscodeViewModel)
    viewModelOf(::PhraseConfirmViewModel)
    viewModelOf(::PhraseGenerateViewModel)
    viewModelOf(::RecoveryEnterViewModel)
    viewModelOf(::RecentRecordsViewModel)
    viewModelOf(::TransferViewModel)
    viewModelOf(::AddAddressViewModel)
    viewModelOf(::AddressListViewModel)
    viewModelOf(::GasFeeViewModel)
    viewModelOf(::EnterPasscodeViewModel)
    viewModelOf(::ReceiveQrCodeViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::CurrencyViewModel)
    viewModelOf(::TransactionViewModel)
    viewModelOf(::ShowPhraseViewModel)
    viewModelOf(::PrivateKeyViewModel)
}
