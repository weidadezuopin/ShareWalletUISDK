package com.sharedwallet.sdk.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharedwallet.sdk.data.repository.QrCodeRepository
import com.sharedwallet.sdk.data.repository.ToastRepository
import com.sharedwallet.sdk.data.repository.UserRepository
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.fromShortName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ReceiveQrCodeViewModel(
    savedStateHandle: SavedStateHandle,
    userRepository: UserRepository,
    qrCodeRepository: QrCodeRepository,
    private val toastRepository: ToastRepository,
) : ViewModel() {

    private val _addressIdFlow = MutableStateFlow("")
    val addressIdFlow = _addressIdFlow.asStateFlow()

    private val _qrCodeBitmapFlow = MutableStateFlow<UiState<Bitmap>>(UiState.Loading)
    val qrCodeBitmapFlow = _qrCodeBitmapFlow.asStateFlow()

    fun tryToast(message: String) = toastRepository.tryToast(message)

    init {
        viewModelScope.launch {
            val currency: CurrencyType? = savedStateHandle.get<String>(Keys.CURRENCY)?.fromShortName
            if (currency != null) {
                _addressIdFlow.value = userRepository.getPublicAddress(currency)
                _qrCodeBitmapFlow.value =
                    UiState.Success(qrCodeRepository.generate(addressIdFlow.value))
            }
        }
    }
}
