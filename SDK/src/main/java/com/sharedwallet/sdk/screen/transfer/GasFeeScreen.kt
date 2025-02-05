package com.sharedwallet.sdk.screen.transfer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.FeePriority
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.cards.GasFeeItem
import com.sharedwallet.sdk.reusable.dialog.InfoDialog
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.viewmodel.GasFeeViewModel

@Composable
internal fun GasFeeScreen(
    viewModel: GasFeeViewModel = koinSwViewModel(),
    feeCurrency: CurrencyType,
    onBackClick: () -> Unit = { },
    onSelectGasPrice: (price: Double) -> Unit = { },
) {
    val selectedPriority by viewModel.selectedPriorityFlow.collectAsStateWithLifecycle()

    var minerFeeInfoShown by rememberSaveable { mutableStateOf(false) }

    WalletScaffold(
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_set_gas_fee),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(state = rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = stringResource(id = R.string.sw_set_priority))
            Spacer(modifier = Modifier.height(20.dp))
            viewModel.gasPrices.forEachIndexed { index, gasPrice ->
                GasFeeItem(
                    feeCurrency = feeCurrency,
                    isSelected = selectedPriority == gasPrice,
                    gasPrice = gasPrice,
                    feePriority = when(index) {
                        0 -> FeePriority.Fast
                        1 -> FeePriority.Medium
                        else -> FeePriority.Slow
                    },
                    onClick = { onSelectGasPrice(gasPrice) },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(20.dp))
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
}

@Preview(showSystemUi = true)
@Composable
fun PreviewGasFeeScreen() {
    SharedWalletTheme {
        GasFeeScreen(
            feeCurrency = CurrencyType.ETH,
        )
    }
}
