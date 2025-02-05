@file:OptIn(ExperimentalCoroutinesApi::class)

package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharedwallet.sdk.data.repository.*
import com.sharedwallet.sdk.domain.models.Currency
import com.sharedwallet.sdk.domain.models.Record
import com.sharedwallet.sdk.domain.state.RequestState
import com.sharedwallet.sdk.domain.utils.successData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class HomeViewModel(
    paperCurrencyRepository: PaperCurrencyRepository,
    private val tutorialRepository: TutorialRepository,
    private val currencyRepository: CurrencyRepository,
    private val currencyFormatter: CurrencyFormatter,
    private val recentRecordsRepository: RecentRecordsRepository,
): ViewModel() {

    val tutorialFlow = tutorialRepository.tutorialFlow

    private val _loadingFlow = MutableStateFlow(true)
    val loadingFlow = _loadingFlow.asStateFlow()

    private val _currenciesFlow = MutableStateFlow<List<Currency>>(emptyList())
    val currenciesFlow = _currenciesFlow.asStateFlow()

    private val _allBalanceFlow = MutableStateFlow("0.00")
    val allBalanceFlow = _allBalanceFlow.asStateFlow()

    private val _recentRecordFlow = MutableStateFlow<Record?>(null)
    val recentRecordFlow = _recentRecordFlow.asStateFlow()

    val selectedCurrencySymbolFlow = paperCurrencyRepository.selected

    fun setTutorialShown() {
        viewModelScope.launch {
            tutorialRepository.setShown()
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _loadingFlow.value = true
            val recentRecordRequest = recentRecordsRepository.getLastRecord()
            if (recentRecordRequest is RequestState.Success) {
                _recentRecordFlow.value = recentRecordRequest.data
            }
            val availableTypes = currencyRepository.getAvailableCurrencies()
            if (_currenciesFlow.value.isEmpty()) {
                _currenciesFlow.value = availableTypes.map { currencyType ->
                    Currency(
                        type = currencyType,
                        balance = "0",
                        price = "0.00",
                    )
                }
            }
            val coinRatiosRequest = async { currencyRepository.getCoinRatios() }
            val balanceRequests = availableTypes.map { currency ->
                async {
                    val balance = when (val request = currencyRepository.getBalance(currency)) {
                        is RequestState.Success -> request.data
                        is RequestState.Error -> _currenciesFlow.value
                            .firstOrNull { it.type == currency }
                            ?.balance?.toDoubleOrNull()
                            ?: 0.0
                    }
                    RequestState.Success(currency to balance)
                }
            }
            buildList {
                add(coinRatiosRequest)
                addAll(balanceRequests)
            }
                .awaitAll()

            val balances = balanceRequests.associate { it.getCompleted().successData() }

            val prices = balanceRequests.map { it.getCompleted().successData() }.associate { pair ->
                val price = pair.second * ((coinRatiosRequest.getCompleted() as? RequestState.Success)?.data?.firstOrNull { it.type == pair.first }?.ratio
                        ?: 0.0)
                pair.first to price
            }

            _allBalanceFlow.value = currencyFormatter.formatPrice(prices.values.sum())
            _currenciesFlow.value = availableTypes.map { currencyType ->
                Currency(
                    type = currencyType,
                    balance = currencyFormatter.formatAmount(balances[currencyType]!!, currencyType),
                    price = currencyFormatter.formatPrice(prices[currencyType]!!),
                )
            }
            _loadingFlow.value = false
        }
    }
}
