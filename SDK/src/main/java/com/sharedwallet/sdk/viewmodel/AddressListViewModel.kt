package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharedwallet.sdk.data.repository.LocalAddressRepository
import com.sharedwallet.sdk.data.repository.ToastRepository
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.models.Address
import com.sharedwallet.sdk.domain.state.RequestState
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.fromShortName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class AddressListViewModel(
    savedStateHandle: SavedStateHandle,
    private val toastRepository: ToastRepository,
    private val addressRepository: LocalAddressRepository,
) : ViewModel() {

    val currencyType: CurrencyType = savedStateHandle.get<String>(Keys.CURRENCY)?.fromShortName!!

    private val _addressListFlow = MutableStateFlow<UiState<List<Address>>>(UiState.Loading)
    val addressListFlow = _addressListFlow.asStateFlow()

    fun tryToast(message: String) = toastRepository.tryToast(message)

    fun deleteAddress(address: Address) {
        viewModelScope.launch {
            val isDeleted = addressRepository.deleteAddress(currencyType, address)
            if (isDeleted) {
                _addressListFlow.update { state ->
                    when(state) {
                        is UiState.Success -> UiState.Success(state.data - address)
                        else -> state
                    }
                }
            }
        }
    }

    fun loadList() {
        viewModelScope.launch {
            _addressListFlow.value = UiState.Loading
            when (val request = addressRepository.getAddressList(currencyType)) {
                is RequestState.Error -> {
                    _addressListFlow.value = UiState.Error(request.e)
                }
                is RequestState.Success -> {
                    _addressListFlow.value = UiState.Success(request.data)
                }
            }
        }
    }
}
