package com.sharedwallet.sdk.screen.currency

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.models.Record
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.ErrorText
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.cards.CurrencyCard
import com.sharedwallet.sdk.reusable.errorMessage
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.viewmodel.CurrencyViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun CurrencyDetailsScreen(
    viewModel: CurrencyViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onRecordClick: (record: Record) -> Unit = { },
    onTransferClick: () -> Unit = { },
    onReceiveClick: () -> Unit = { },
) {
    val currencyDetailState by viewModel.currencyDetailsStateFlow.collectAsStateWithLifecycle()
    val currencyDetails by viewModel.currencyDetailsFlow.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState()
    val allRecords = viewModel.allRecordsFlow.collectAsLazyPagingItems()
    val sentRecords = viewModel.sentRecordsFlow.collectAsLazyPagingItems()
    val receivedRecords = viewModel.receivedRecordsFlow.collectAsLazyPagingItems()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadData()
        // Delay is important to refresh paging lists
        delay(200)
        allRecords.refresh()
        sentRecords.refresh()
        receivedRecords.refresh()
    }

    WalletScaffold(
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_currency_details),
            )
        },
    ) {
        Column {
            Spacer(modifier = Modifier.height(28.dp))
            CurrencyCard(
                modifier = Modifier.padding(horizontal = 20.dp),
                currency = currencyDetails.currencyType,
                value = currencyDetails.balance,
                hasRefresh = true,
                loadState = currencyDetailState,
                onRefreshClick = { viewModel.loadData() },
            )
            Spacer(modifier = Modifier.height(4.dp))
            AnimatedVisibility(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .align(Alignment.End),
                visible = currencyDetailState is UiState.Error,
            ) {
                val state = currencyDetailState
                ErrorText(
                    text = if (state is UiState.Error) {
                        errorMessage(e = state.e)
                    } else {
                        ""
                    },
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            TransactionHistoryCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                pagerState = pagerState,
                allRecords = allRecords,
                sentRecords = sentRecords,
                receivedRecords = receivedRecords,
                onRecordClick = onRecordClick,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp)
                    .background(color = Color.White),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.width(48.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onTransferClick,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.sw_transfer),
                        style = MaterialTheme.typography.body1,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onReceiveClick,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black,
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.sw_receive),
                        style = MaterialTheme.typography.body1,
                    )
                }
                Spacer(modifier = Modifier.width(48.dp))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewCurrencyDetails() {
    SharedWalletTheme {
        CurrencyDetailsScreen()
    }
}
