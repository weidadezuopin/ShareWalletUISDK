package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sharedwallet.sdk.data.repository.ToastRepository
import com.sharedwallet.sdk.data.repository.UserRepository
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.PrivateKeyFormat
import com.sharedwallet.sdk.domain.utils.fromShortName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal class PrivateKeyViewModel(
    savedStateHandle: SavedStateHandle,
    userRepository: UserRepository,
    private val toastRepository: ToastRepository,
) : ViewModel() {

    private val passcode = savedStateHandle.get<String>(Keys.CONFIRMED_CODE)!!
    private val currency: CurrencyType = savedStateHandle.get<String>(Keys.CURRENCY)?.fromShortName!!

    private val _selectedFormatFlow = MutableStateFlow(PrivateKeyFormat.Hex)

    val privateKeyFlow = _selectedFormatFlow.map {
        userRepository.getPrivateKey(passcode, currency, it)
    }

    fun tryToast(message: String) = toastRepository.tryToast(message)
}
