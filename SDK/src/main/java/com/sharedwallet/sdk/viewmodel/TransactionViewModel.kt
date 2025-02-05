package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharedwallet.sdk.data.repository.ToastRepository
import com.sharedwallet.sdk.data.repository.TransactionEventRepository
import com.sharedwallet.sdk.data.repository.TransactionRepository
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.models.Transaction
import com.sharedwallet.sdk.domain.state.RequestState
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.fromShortName
import com.sharedwallet.sdk.domain.utils.successData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class TransactionViewModel(
    savedStateHandle: SavedStateHandle,
    transactionEventRepository: TransactionEventRepository,
    private val transactionRepository: TransactionRepository,
    private val toastRepository: ToastRepository,
) : ViewModel() {

    private val currency: CurrencyType = savedStateHandle.get<String>(Keys.CURRENCY)?.fromShortName!!
    private val transactionHash = savedStateHandle.get<String>(Keys.TRANS_ID)!!

    private val _transactionFlow = MutableStateFlow<UiState<Transaction>>(UiState.Loading)
    val transactionFlow = _transactionFlow.asStateFlow()

    private val _transactionUriFlow = MutableStateFlow<String?>(null)
    val transactionUriFlow = _transactionUriFlow.asStateFlow()

    fun tryToast(message: String) = toastRepository.tryToast(message)

    fun loadData() {
        viewModelScope.launch {
            _transactionFlow.value = UiState.Loading
            val transactionRequest =
                async { transactionRepository.getTransaction(currency, transactionHash) }
            listOf(
                transactionRequest,
            ).awaitAll().let { list ->
                _transactionFlow.value = if (list.all { it is RequestState.Success }) {
                    UiState.Success(
                        transactionRequest.getCompleted().successData()
                    )
                } else {
                    UiState.Error(
                        list.filterIsInstance<RequestState.Error>().first().e
                    )
                }
            }
        }
    }

    init {
        _transactionUriFlow.value = when(currency) {
            CurrencyType.BTC -> "https://www.blockchain.com/btc/tx/$transactionHash"
            CurrencyType.ETH,
            CurrencyType.USDT_ERC20 -> "https://etherscan.io/tx/$transactionHash"
            CurrencyType.TRX,
            CurrencyType.USDT_TRC20 -> "https://tronscan.io/#/transaction/$transactionHash"
        }
        viewModelScope.launch {
            transactionEventRepository.forTransaction(transactionHash).collect {
                loadData()
            }
        }
    }
}
