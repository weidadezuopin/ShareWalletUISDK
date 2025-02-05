package com.sharedwallet.sdk.domain.models

import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.PaperCurrency
import com.sharedwallet.sdk.domain.state.FeeRefreshState
import com.sharedwallet.sdk.domain.state.FieldError
import com.sharedwallet.sdk.domain.state.UiState

data class Transfer(
    val currency: CurrencyType,

    val initState: UiState<Unit> = UiState.Loading,
    val transferState: UiState<String>? = null,

    val toAddress: String = "",
    val toAddressError: FieldError? = null,
    val myAddress: String = "",

    val amount: String = "",
    val receivedAmount: String = "",
    val amountError: FieldError? = null,
    val amountEqualTo: String = "",
    val allBalance: String = "",
    val isAllPressed: Boolean = false,
    val ratio: Double = 0.0,
    val feeRatio: Double = 0.0,
    val paperCurrency: PaperCurrency = PaperCurrency.YUAN,

    val gasPrices: List<Double> = listOf(),
    val selectedPriorityIndex: Int = -1,
    val feeRefreshState: FeeRefreshState = FeeRefreshState.Loading,
    val estimatedFeeError: Exception? = null,
    val estimatedFee: String = "",
    val estimatedFeeEqualTo: String = "",
    val feeEquation: String = "",
) {

    val hasError: Boolean
        get() = toAddressError != null || amountError != null

    val selectedGasPrice: Double
        get() = gasPrices.getOrNull(selectedPriorityIndex) ?: 0.0
}
