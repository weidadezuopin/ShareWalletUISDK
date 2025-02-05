package com.sharedwallet.sdk.screen.transfer

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.models.Transfer
import com.sharedwallet.sdk.domain.state.FieldError
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.feeCurrency
import com.sharedwallet.sdk.domain.utils.hasMinerFee
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.ErrorText
import com.sharedwallet.sdk.reusable.SwCircleIcon
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.buttons.PrimaryButton
import com.sharedwallet.sdk.reusable.dialog.InfoDialog
import com.sharedwallet.sdk.reusable.dialog.LoadingDialog
import com.sharedwallet.sdk.reusable.errorMessage
import com.sharedwallet.sdk.reusable.field.CurrencyAddressField
import com.sharedwallet.sdk.reusable.progress.SmallLoadingProgress
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme
import com.sharedwallet.sdk.viewmodel.TransferViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
internal fun TransferScreen(
    selectedAddressId: String? = null,
    selectedGasFee: Double? = null,
    confirmedPasscode: String? = null,
    viewModel: TransferViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onAddressListClick: () -> Unit = { },
    onScanClick: () -> Unit = { },
    onMinerFeeClick: (gasPrices: List<Double>, selectedFee: Double) -> Unit = { _, _ -> },
    onPasscodeRequest: () -> Unit = { },
    onConsumePasscodeRequest: () -> Unit = { },
    onConsumeSelectedAddressRequest: () -> Unit = { },
    onConsumeSelectedGasRequest: () -> Unit = { },
    onTransferSent: (id: String) -> Unit = { },
) {
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = confirmedPasscode) {
        if (confirmedPasscode != null) {
            viewModel.transfer(confirmedPasscode)
            onConsumePasscodeRequest()
        }
    }

    LaunchedEffect(key1 = selectedAddressId) {
        if (selectedAddressId != null) {
            viewModel.updateAddress(selectedAddressId)
            onConsumeSelectedAddressRequest()
        }
    }

    LaunchedEffect(key1 = selectedGasFee) {
        if (selectedGasFee != null) {
            viewModel.updateSelectedGasFee(selectedGasFee)
            onConsumeSelectedGasRequest()
        }
    }

    val transfer by viewModel.transferFlow.collectAsStateWithLifecycle()

    var minerFeeInfoShown: Boolean by rememberSaveable { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    BackHandler(enabled = bottomSheetState.isVisible) {
        coroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    LaunchedEffect(key1 = transfer.transferState) {
        val state = transfer.transferState
        if (state is UiState.Success) {
            onTransferSent(state.data)
        }
    }

    WalletScaffold(
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(
                    id = R.string.sw_currency_transfer,
                    transfer.currency.shortName,
                ),
            )
        },
    ) {
        AnimatedContent(targetState = transfer.initState) { state ->
            when (state) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        SmallLoadingProgress(modifier = Modifier.align(Alignment.Center))
                    }
                }
                is UiState.Error -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier.weight(1f))
                        ErrorText(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = errorMessage(e = state.e),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = { viewModel.loadData() },
                        ) {
                            Text(text = stringResource(id = R.string.sw_try_again))
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                is UiState.Success -> {
                    Content(
                        transfer = transfer,
                        viewModel = viewModel,
                        onAddressListClick = onAddressListClick,
                        onScanClick = onScanClick,
                        onMinerFeeClick = {
                            onMinerFeeClick(
                                transfer.gasPrices,
                                transfer.selectedGasPrice,
                            )
                        },
                        onMinerInfoClick = { minerFeeInfoShown = true },
                    ) {
                        if (viewModel.verify()) {
                            keyboardController?.hide()
                            coroutineScope.launch { bottomSheetState.show() }
                        }
                    }
                }
            }
        }
    }
    if (minerFeeInfoShown) {
        InfoDialog(
            onDismissRequest = { minerFeeInfoShown = false },
            title = { SwIcon(iconId = R.drawable.sw_ic_miner_fee) },
            text = {
                Text(
                    text = stringResource(id = R.string.sw_miners_fee_info),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center,
                )
            },
            actionText = stringResource(id = R.string.sw_got_it),
            onActionClick = { minerFeeInfoShown = false }
        )
    }
    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
        sheetContent = {
            PaymentDetailsBottomSheet(
                currency = transfer.currency,
                toAddressId = transfer.toAddress,
                fromAddressId = transfer.myAddress,
                amount = transfer.receivedAmount,
                minerFee = transfer.estimatedFee,
                minerFeeEquation = transfer.feeEquation,
                onCloseClick = {
                    coroutineScope.launch { bottomSheetState.hide() }
                },
                onNextClick = {
                    coroutineScope.launch { bottomSheetState.snapTo(ModalBottomSheetValue.Hidden) }
                    onPasscodeRequest()
                },
            )
        }
    ) {}
    when(val state = transfer.transferState) {
        is UiState.Loading -> LoadingDialog()
        is UiState.Error -> {
            InfoDialog(
                onDismissRequest = { viewModel.resetTransferState() },
                text = {
                    ErrorText(
                        text = if (state.e.message?.startsWith("3005") == true) {
                            stringResource(
                                id = R.string.sw_balance_less_than_fee_message,
                                transfer.currency.feeCurrency.shortName, transfer.estimatedFee,
                            )
                        } else {
                            errorMessage(e = state.e)
                        }
                    )
                },
                actionText = stringResource(id = R.string.sw_got_it),
                onActionClick = { viewModel.resetTransferState() }
            )
        }
        else -> { /* Do nothing */ }
    }
}

@Composable
private fun Content(
    transfer: Transfer,
    viewModel: TransferViewModel,
    onAddressListClick: () -> Unit = { },
    onScanClick: () -> Unit = { },
    onMinerFeeClick: () -> Unit = { },
    onMinerInfoClick: () -> Unit = { },
    onNextClick: () -> Unit = { },
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(state = rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(28.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.sw_receiving_address))
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onAddressListClick) {
                SwCircleIcon(
                    size = 36.dp,
                    iconId = R.drawable.sw_ic_address,
                    color = SwTheme.colors.grayButton.copy(alpha = .5f),
                )
            }
            IconButton(onClick = onScanClick) {
                SwCircleIcon(
                    size = 36.dp,
                    iconId = R.drawable.sw_ic_scan,
                    color = SwTheme.colors.grayButton.copy(alpha = .5f),
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        CurrencyAddressField(
            currency = transfer.currency,
            value = transfer.toAddress,
            onValueChange = {
                viewModel.updateAddress(it)
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        val addressError = transfer.toAddressError
        AnimatedVisibility(visible = addressError != null) {
            ErrorText(text = when(addressError) {
                is FieldError.Empty -> stringResource(id = R.string.sw_address_empty_message)
                is FieldError.Invalid -> stringResource(id = R.string.sw_address_invalid)
                null -> ""
            })
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(id = R.string.sw_transfer_amount))
        Spacer(modifier = Modifier.height(16.dp))
        TransferAmountCard(
            currency = transfer.currency,
            amount = transfer.amount,
            amountEqualTo = "${transfer.paperCurrency.symbol}${transfer.amountEqualTo}",
            amountError = transfer.amountError,
            allBalance = transfer.allBalance,
            onAmountChanged = { viewModel.setAmount(it) },
            onAllClick = { viewModel.setAllBalanceToAmount() },
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (transfer.currency.hasMinerFee) {
            Spacer(modifier = Modifier.height(20.dp))
            MinerFeeCard(
                currency = transfer.currency,
                fee = transfer.estimatedFee,
                feeEqualTo = "${transfer.paperCurrency.symbol}${transfer.estimatedFeeEqualTo}",
                feeRefreshState = transfer.feeRefreshState,
                onClick = onMinerFeeClick,
                onInfoClick = onMinerInfoClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(visible = transfer.estimatedFeeError != null) {
                ErrorText(text = errorMessage(e = transfer.estimatedFeeError))
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onNextClick,
            text = stringResource(id = R.string.sw_next),
        )
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTransfer() {
    SharedWalletTheme {
        TransferScreen()
    }
}
