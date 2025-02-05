package com.sharedwallet.sdk.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.compose.clickableNoRipple
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.PaperCurrency
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.cards.*
import com.sharedwallet.sdk.reusable.dialog.MessageTwoButtonsDialog
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme
import com.sharedwallet.sdk.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onRecordsClick: () -> Unit = { },
    onCurrencyClick: (currency: CurrencyType) -> Unit = { },
) {
    val context = LocalContext.current
    var isShown by rememberSaveable { mutableStateOf(true) }
    val isInfoPointerShown by viewModel.tutorialFlow.collectAsStateWithLifecycle(false)
    var isBuyDialogWarningShown by rememberSaveable { mutableStateOf(false) }

    val lastRecord by viewModel.recentRecordFlow.collectAsStateWithLifecycle()
    val currencies by viewModel.currenciesFlow.collectAsStateWithLifecycle()
    val allBalance by viewModel.allBalanceFlow.collectAsStateWithLifecycle("0.00")
    val paperCurrency by viewModel.selectedCurrencySymbolFlow.collectAsStateWithLifecycle(PaperCurrency.YUAN)

    val refreshing by viewModel.loadingFlow.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.refreshData() })

    LaunchedEffect(key1 = Unit) {
        viewModel.refreshData()
    }

    WalletScaffold(
        topBar = { DefaultActionBar(onBackClick = onBackClick) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .clipToBounds(),
        ) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                CurrencyBalanceCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 8.dp, end = 20.dp),
                    balance = if (isShown) {
                        "${paperCurrency.symbol}$allBalance"
                    } else {
                        "${paperCurrency.symbol}*******"
                    },
                    isShown = isShown,
                    onVisibilityClick = { isShown = !isShown },
                    onBuyClick = { isBuyDialogWarningShown = true },
                )
                val record = lastRecord
                AnimatedVisibility(visible = record != null) {
                    if(record != null) {
                        LatestTransactionCard(
                            record = record,
                            onRecordsClick = onRecordsClick,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    modifier = Modifier.padding(start = 36.dp),
                    text = stringResource(id = R.string.sw_assets),
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.height(12.dp))
                val currencyHeight = 78.dp
                Box(modifier = Modifier.height(currencyHeight * (currencies.size + 1))) {
                    currencies.forEachIndexed { index, currency ->
                        Column {
                            Spacer(modifier = Modifier.height(currencyHeight * index))
                            AssetCurrencyBottomCard(
                                modifier = Modifier.height(currencyHeight * 2),
                                currency = currency.type,
                                balance = if (isShown) currency.balance else "********",
                                price = if (isShown) {
                                    "≈${paperCurrency.symbol}${currency.price}"
                                } else {
                                    "≈${paperCurrency.symbol}*****"
                                },
                            ) { onCurrencyClick(currency.type) }
                        }
                    }
                    Column {
                        Spacer(modifier = Modifier.height(currencyHeight * currencies.size))
                        AssetBottomCard(
                            modifier = Modifier.height(currencyHeight),
                            color = MaterialTheme.colors.background,
                        ) {
                        }
                    }
                }
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = refreshing,
                state = pullRefreshState,
                contentColor = MaterialTheme.colors.primary,
            )
        }
    }
    if (isInfoPointerShown) {
        Dialog(onDismissRequest = { viewModel.setTutorialShown() }) {
            Column(
                modifier = Modifier.clickableNoRipple { viewModel.setTutorialShown() },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(280.dp))
                SwIcon(iconId = R.drawable.sw_ic_finger_point)
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    backgroundColor = SwTheme.colors.secondary,
                    shape = RoundedCornerShape(12.dp),
                    elevation = 0.dp,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 32.dp, vertical = 20.dp),
                        text = stringResource(id = R.string.sw_choose_currency_from_here),
                        fontSize = 20.sp,
                    )
                }
            }
        }
    }
    if (isBuyDialogWarningShown) {
        MessageTwoButtonsDialog(
            onCancelClick = { isBuyDialogWarningShown = false },
            onOkClick = {
                isBuyDialogWarningShown = false
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.moonpay.com/buy"),
                    )
                )
            },
            text = stringResource(id = R.string.sw_going_third_party_warning),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewHomeScreen() {
    SharedWalletTheme {
        HomeScreen()
    }
}
