package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharedwallet.sdk.data.repository.LocalAddressRepository
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.models.Address
import com.sharedwallet.sdk.domain.state.RequestState
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.fromShortName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

internal class AddAddressViewModel(
    savedStateHandle: SavedStateHandle,
    private val addressRepository: LocalAddressRepository,
) : ViewModel() {

    private val currencyType: CurrencyType = savedStateHandle.get<String>(Keys.CURRENCY)?.fromShortName!!

    val nameTextFlow = MutableStateFlow("")
    val addressTextFlow = MutableStateFlow(savedStateHandle[Keys.ADDRESS_ID] ?: "")

    private val _saveStateFlow = MutableStateFlow<UiState<Unit>?>(null)
    val saveStateFlow = _saveStateFlow.asStateFlow()

    private val _isFieldsValidFlow = MutableStateFlow(true)
    val isFieldsValidFlow = _isFieldsValidFlow.asStateFlow()

    fun save() {
        if (!isValid()) {
            return
        }
        viewModelScope.launch {
            _saveStateFlow.value = UiState.Loading
            val request = addressRepository.addAddress(
                currencyType = currencyType,
                address = Address(
                    id = addressTextFlow.value.trim(),
                    name = nameTextFlow.value.trim(),
                ),
            )
            _saveStateFlow.value = when (request) {
                is RequestState.Error -> UiState.Error(request.e)
                is RequestState.Success -> if(request.data) {
                    UiState.Success(Unit)
                } else {
                    UiState.Error(IllegalArgumentException(""))
                }
            }
        }
    }

    private fun isValid(): Boolean {
        return if (nameTextFlow.value.isBlank() || addressTextFlow.value.isBlank()) {
            _isFieldsValidFlow.value = false
            false
        } else {
            true
        }
    }

    init {
        val addressId: String? = savedStateHandle[Keys.ADDRESS_ID]
        if (addressId != null) {
            addressTextFlow.value = addressId
        }
        viewModelScope.launch {
            merge(nameTextFlow, addressTextFlow).collect {
                _saveStateFlow.value = null
                _isFieldsValidFlow.value = true
            }
        }
    }
}
