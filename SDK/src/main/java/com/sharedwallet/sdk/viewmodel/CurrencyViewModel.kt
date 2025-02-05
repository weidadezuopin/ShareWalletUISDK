package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sharedwallet.sdk.data.repository.CurrencyFormatter
import com.sharedwallet.sdk.data.repository.CurrencyRepository
import com.sharedwallet.sdk.data.repository.RecordsRepository
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.TransactionType
import com.sharedwallet.sdk.domain.models.CurrencyDetails
import com.sharedwallet.sdk.domain.state.RequestState
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.fromShortName
import com.sharedwallet.sdk.domain.utils.successData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalCoroutinesApi::class)
internal class CurrencyViewModel(
    savedStateHandle: SavedStateHandle,
    recordsRepository: RecordsRepository,
    private val currencyRepository: CurrencyRepository,
    private val currencyFormatter: CurrencyFormatter,
) : ViewModel() {

    val currency: CurrencyType = savedStateHandle.get<String>(Keys.CURRENCY)?.fromShortName!!

    private val _currencyDetailsStateFlow = MutableStateFlow<UiState<Unit>?>(UiState.Loading)
    val currencyDetailsStateFlow = _currencyDetailsStateFlow.asStateFlow()
    private val _currencyDetailsFlow = MutableStateFlow(CurrencyDetails(currency))
    val currencyDetailsFlow = _currencyDetailsFlow.asStateFlow()

    val allRecordsFlow = recordsRepository.getRecords(currency, TransactionType.All)
        .cachedIn(viewModelScope)
    val sentRecordsFlow = recordsRepository.getRecords(currency, TransactionType.Sent)
        .cachedIn(viewModelScope)
    val receivedRecordsFlow = recordsRepository.getRecords(currency, TransactionType.Received)
        .cachedIn(viewModelScope)

    fun loadData() {
        _currencyDetailsStateFlow.value = UiState.Loading
        viewModelScope.launch {
            delay(400) // For loading animation
            val amountRequest = async { currencyRepository.getBalance(currency) }
            listOf(
                amountRequest,
            ).awaitAll().let { list ->
                _currencyDetailsStateFlow.value = if (list.all { it is RequestState.Success }) {
                    UiState.Success(Unit)
                } else {
                    UiState.Error(
                        list.filterIsInstance<RequestState.Error>().first().e
                    )
                }
                if (amountRequest.getCompleted() is RequestState.Success) {
                    _currencyDetailsFlow.value = CurrencyDetails(
                        currencyType = currency,
                        balance = currencyFormatter.formatAmount(
                            value = amountRequest.getCompleted().successData(),
                            currencyType = currency,
                        ),
                        bandwidthEnergy = null,
                        bandwidthPoints = null,
                    )
                    delay(1_500)
                    _currencyDetailsStateFlow.value = null
                }
            }
        }
    }
}
