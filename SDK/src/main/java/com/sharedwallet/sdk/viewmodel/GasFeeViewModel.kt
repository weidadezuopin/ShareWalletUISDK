package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sharedwallet.sdk.domain.constants.Keys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class GasFeeViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val selectedFee = savedStateHandle.get<String>(Keys.SELECTED_GAS_FEE)?.toDouble()!!
    val gasPrices = savedStateHandle.get<String>(Keys.GAS_PRICES)
        ?.split(',')?.map { it.toDouble() }!!

    private val _selectedPriorityFlow = MutableStateFlow(selectedFee)
    val selectedPriorityFlow = _selectedPriorityFlow.asStateFlow()

    fun select(priority: Double) {
        _selectedPriorityFlow.update { priority }
    }
}
