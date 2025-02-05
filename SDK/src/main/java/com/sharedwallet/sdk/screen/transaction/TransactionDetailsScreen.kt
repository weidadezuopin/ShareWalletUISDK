package com.sharedwallet.sdk.screen.transaction

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.RecordState
import com.sharedwallet.sdk.domain.models.Transaction
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.*
import com.sharedwallet.sdk.reusable.*
import com.sharedwallet.sdk.reusable.buttons.PrimaryButton
import com.sharedwallet.sdk.reusable.cards.CurrencyCard
import com.sharedwallet.sdk.reusable.progress.SmallLoadingProgress
import com.sharedwallet.sdk.temp.fakeSucceedTransaction
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.viewmodel.TransactionViewModel


@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun TransactionDetailsScreen(
    viewModel: TransactionViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onSaveIdClick: (otherId: String) -> Unit = { },
) {
    val context = LocalContext.current
    val transactionState by viewModel.transactionFlow.collectAsStateWithLifecycle()
    val transactionUri by viewModel.transactionUriFlow.collectAsStateWithLifecycle()

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadData()
    }

    WalletScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = when (val state = transactionState) {
                    is UiState.Success ->
                        stringResource(id = if (state.data.isSent) R.string.sw_transfer_details else R.string.sw_deposit_details)
                    else -> ""
                },
            )
        },
    ) {
        AnimatedContent(targetState = transactionState) { state ->
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
                        transaction = state.data,
                        onDoneClick = onBackClick,
                        onSaveIdClick = onSaveIdClick,
                        onTransactionIdClick = {
                            if (transactionUri != null) {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(transactionUri),
                                    )
                                )
                            }
                        },
                        onCopyClick = { text ->
                            context.copyToClipboard(text)
                            viewModel.tryToast(context.getString(R.string.sw_copied))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    transaction: Transaction,
    onDoneClick: () -> Unit = { },
    onTransactionIdClick: () -> Unit = { },
    onSaveIdClick: (otherId: String) -> Unit = { },
    onCopyClick: (text: String) -> Unit = { },
) {
    Column(
        modifier = Modifier.verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SwIcon(modifier = Modifier.size(62.dp), iconId = transaction.stateIconRes)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = transaction.state.stringRes),
            color = transaction.state.color(),
        )
        Spacer(modifier = Modifier.height(40.dp))
        CurrencyCard(
            modifier = Modifier.padding(horizontal = 20.dp),
            currency = transaction.currency,
            value = transaction.amount,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            backgroundColor = Color.White,
            shape = RoundedCornerShape(10.dp),
            elevation = 8.dp,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if(transaction.state != RecordState.Pending) {
                    TransactionTimeFeesCard(transaction = transaction)
                }
                Spacer(modifier = Modifier.height(16.dp))
                TransactionIdsCard(
                    transaction = transaction,
                    onSaveIdClick = onSaveIdClick,
                    onTransactionIdClick = onTransactionIdClick,
                    onCopyClick = onCopyClick,
                )
            }
        }
        if (transaction.state == RecordState.Pending) {
            Spacer(modifier = Modifier.height(20.dp))
            PrimaryButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                onClick = onDoneClick,
                text = stringResource(id = R.string.sw_done),
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTransactionDetails() {
    SharedWalletTheme {
        Content(
            transaction = fakeSucceedTransaction,
        )
    }
}
