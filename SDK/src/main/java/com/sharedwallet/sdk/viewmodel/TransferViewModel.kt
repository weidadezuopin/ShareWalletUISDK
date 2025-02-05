package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharedwallet.sdk.data.repository.*
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.models.Transfer
import com.sharedwallet.sdk.domain.models.TransferEvent
import com.sharedwallet.sdk.domain.state.FeeRefreshState
import com.sharedwallet.sdk.domain.state.FieldError
import com.sharedwallet.sdk.domain.state.RequestState
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
internal class TransferViewModel(
    savedStateHandle: SavedStateHandle,
    private val paperCurrencyRepository: PaperCurrencyRepository,
    private val userRepository: UserRepository,
    private val currencyRepository: CurrencyRepository,
    private val currencyFormatter: CurrencyFormatter,
    private val transferEventRepository: TransferEventRepository,
) : ViewModel() {

    val currency: CurrencyType = savedStateHandle.get<String>(Keys.CURRENCY)?.fromShortName!!
    private val otherUserId = savedStateHandle.get<String>(Keys.OTHER_USER_ID)

    private val _transferFlow = MutableStateFlow(
        Transfer(
            currency = currency,
        )
    )
    val transferFlow = _transferFlow.asStateFlow()

    fun updateAddress(newText: String) {
        _transferFlow.update { it.copy(toAddress = newText.trim(), toAddressError = null) }
    }

    fun updateSelectedGasFee(newGasFee: Double) {
        _transferFlow.update { it.copy(selectedPriorityIndex = it.gasPrices.indexOf(newGasFee)) }
        if (currency.doesPayFees && _transferFlow.value.isAllPressed) {
            setAmount(amount = "")
        }
    }

    fun setAmount(amount: String, byAllBalance: Boolean = false) {
        val formattedAmount = currencyFormatter.formatEditableAmount(amount.trim(), currency)
        if (formattedAmount != null) {
            _transferFlow.update {
                it.copy(
                    amount = formattedAmount,
                    amountError = null,
                    isAllPressed = byAllBalance,
                )
            }
        }
    }

    fun setAllBalanceToAmount() {
        setAmount(
            amount = if (currency.doesPayFees) {
                val amount = (_transferFlow.value.allBalance.toDoubleOrNull() ?: 0.0) -
                        (_transferFlow.value.estimatedFee.toDoubleOrNull() ?: 0.0)
                currencyFormatter.formatAmount(
                    value = amount.coerceAtLeast(0.0),
                    currencyType = currency,
                )
            } else {
                _transferFlow.value.allBalance
            },
            byAllBalance = true,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadData() {
        viewModelScope.launch {
            _transferFlow.update { it.copy(initState = UiState.Loading) }
            val allBalanceRequest = async { currencyRepository.getBalance(currency) }
            val gasPricesRequest = async { currencyRepository.getGasPrices(currency) }
            val coinRatiosRequest = async { currencyRepository.getCoinRatios() }
            val otherUserAddressRequest = async {
                if (!otherUserId.isNullOrEmpty()) {
                    userRepository.getPublicAddressForUser(currency, otherUserId)
                } else {
                    RequestState.Success("")
                }
            }
            val requests = listOf(
                allBalanceRequest, gasPricesRequest, coinRatiosRequest, otherUserAddressRequest,
            )
                .awaitAll()
            val firstError = requests.firstError()
            _transferFlow.update { transfer ->
                if (firstError != null) {
                    transfer.copy(
                        initState = UiState.Error(firstError.e),
                    )
                } else {
                    val priorities = gasPricesRequest.getCompleted().successData()
                    transfer.copy(
                        initState = UiState.Success(Unit),
                        allBalance = currencyFormatter.formatAmount(
                            value = allBalanceRequest.getCompleted().successData(),
                            currencyType = currency,
                        ),
                        ratio = coinRatiosRequest.getCompleted().successData()
                            .firstOrNull { it.type == currency }?.ratio ?: 0.0,
                        feeRatio = coinRatiosRequest.getCompleted().successData()
                            .firstOrNull { it.type == currency.feeCurrency }?.ratio ?: 0.0,
                        gasPrices = priorities,
                        selectedPriorityIndex = if (priorities.size > 1) 1 else -1,
                        myAddress = userRepository.getPublicAddress(currency),
                        paperCurrency = paperCurrencyRepository.getCurrency(),
                        toAddress = otherUserAddressRequest.getCompleted().successData(),
                    )
                }
            }
        }
    }

    fun resetTransferState() {
        _transferFlow.update { it.copy(transferState = null) }
    }

    fun transfer(passcode: String) {
        if (!verify()) return
        viewModelScope.launch {
            _transferFlow.update { it.copy(transferState = UiState.Loading) }
            val response = currencyRepository.transfer(
                passcode = passcode,
                transfer = transferFlow.value,
            )
            when (response) {
                is RequestState.Error -> {
                    _transferFlow.update {
                        it.copy(transferState = UiState.Error(response.e))
                    }
                }
                is RequestState.Success -> {
                    transferEventRepository.sendEvent(
                        TransferEvent(
                            transactionId = response.data,
                            currencyType = transferFlow.value.currency,
                            amount = currencyFormatter.formatAmount(
                                value = transferFlow.value.amount.toDoubleOrNull() ?: 0.0,
                                currencyType = currency,
                            ),
                        )
                    )
                    _transferFlow.update {
                        it.copy(transferState = UiState.Success(response.data))
                    }
                }
            }
        }
    }

    fun verify(): Boolean {
        if (transferFlow.value.toAddress.isBlank()) {
            _transferFlow.update { it.copy(toAddressError = FieldError.Empty) }
        }

        if (transferFlow.value.amount.isEmpty() || transferFlow.value.amount.toDouble() == 0.0) {
            _transferFlow.update { it.copy(amountError = FieldError.Empty) }
        } else if (transferFlow.value.amount.toDouble() > transferFlow.value.allBalance.toDouble()) {
            _transferFlow.update { it.copy(amountError = FieldError.Invalid(FieldError.REASON_BALANCE_NOT_ENOUGH)) }
        }

        return !transferFlow.value.hasError
    }

    init {
        loadData()
        viewModelScope.launch {
            transferFlow
                .distinctUntilChangedBy { it.amount }
                .collectLatest {
                    _transferFlow.update {
                        it.copy(
                            amountEqualTo = currencyFormatter.formatPrice(
                                (it.amount.toDoubleOrNull() ?: 0.0) * it.ratio
                            ),
                        )
                    }
                }
        }
        viewModelScope.launch {
            transferFlow
                .distinctUntilChanged { old, new ->
                    old.amount == new.amount &&
                            old.estimatedFee == new.estimatedFee
                }
                .collectLatest {
                    _transferFlow.update {
                        it.copy(
                            receivedAmount = currencyFormatter.formatAmount(
                                value = it.amount.toDoubleOrNull() ?: 0.0,
                                currencyType = currency,
                            ),
                        )
                    }
                }
        }
        viewModelScope.launch {
            transferFlow
                .filter { it.initState is UiState.Success }
                .filter { it.currency.hasMinerFee }
                .distinctUntilChangedBy { it.selectedPriorityIndex }
                .collectLatest { _ ->
                    var count = 0
                    while (true) {
                        yield()
                        if (count % REFRESH_RATE == 0) {
                            _transferFlow.update { it.copy(feeRefreshState = FeeRefreshState.Loading) }
                            val request = currencyRepository.getGasPrices(currency)
                            _transferFlow.update {
                                when (request) {
                                    is RequestState.Error -> it.copy(
                                        estimatedFeeError = request.e,
                                    )
                                    is RequestState.Success -> it.copy(
                                        estimatedFeeError = null,
                                        gasPrices = request.data,
                                    )
                                }
                            }
                        }
                        _transferFlow.update {
                            it.copy(feeRefreshState = FeeRefreshState.CountDown(REFRESH_RATE - (count % REFRESH_RATE)))
                        }
                        delay(1.seconds)
                        count++
                    }
                }
        }
        viewModelScope.launch {
            transferFlow
                .filter { it.initState is UiState.Success }
                .filter { it.currency.hasMinerFee }
                .distinctUntilChanged { old, new ->
                    old.selectedPriorityIndex == new.selectedPriorityIndex &&
                            old.gasPrices == new.gasPrices
                }
                .collectLatest { transfer ->
                    val request = currencyRepository.getEstimatedFee(currency, transfer.selectedGasPrice)
                    _transferFlow.update {
                        when (request) {
                            is RequestState.Error -> it.copy(
                                estimatedFeeError = request.e,
                            )
                            is RequestState.Success -> it.copy(
                                estimatedFeeError = null,
                                estimatedFee = currencyFormatter.formatGas(request.data),
                                estimatedFeeEqualTo = currencyFormatter.formatPrice(
                                    request.data * it.feeRatio
                                ),
                                feeEquation = when(it.currency) {
                                    CurrencyType.ETH -> ETH_EQUATION.format(
                                        "21000",
                                        currencyFormatter.formatGas(it.selectedGasPrice),
                                    )
                                    CurrencyType.USDT_ERC20 -> ETH_EQUATION.format(
                                        "70000",
                                        currencyFormatter.formatGas(it.selectedGasPrice),
                                    )
                                    CurrencyType.BTC -> BTC_EQUATION.format(
                                        "19",
                                        "53",
                                        )
                                    else -> ""
                                },
                            )
                        }
                    }
                }
        }
    }

    companion object {
        const val REFRESH_RATE = 14
        const val ETH_EQUATION = "≈Gas(%s)*Gas Price(%s ETH)"
        const val BTC_EQUATION = "≈%s SAT/b*%s bytes"
    }
}
