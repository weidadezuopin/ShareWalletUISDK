package com.sharedwallet.sdk.screen.transaction

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.models.Transaction
import com.sharedwallet.sdk.domain.utils.canShowConfirmations
import com.sharedwallet.sdk.domain.utils.feeCurrency
import com.sharedwallet.sdk.domain.utils.isGasFeesAvailable
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.temp.fakeSucceedTransaction
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun TransactionTimeFeesCard(transaction: Transaction) {
    Card(
        elevation = 0.dp,
        backgroundColor = SwTheme.colors.blueCard,
        shape = RoundedCornerShape(8.dp),
    ) {
        var isExpanded: Boolean by rememberSaveable { mutableStateOf(false) }
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(id = R.string.sw_confirm_time))
                Spacer(modifier = Modifier.weight(1f))
                Text(text = transaction.confirmDate ?: "", style = MaterialTheme.typography.body2)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(id = R.string.sw_network_fee))
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = MaterialTheme.typography.body2.toSpanStyle()) {
                            append(transaction.networkFee ?: "")
                        }
                        withStyle(
                            style = MaterialTheme.typography.body1.toSpanStyle()
                                .copy(fontSize = 14.sp),
                        ) {
                            append(transaction.currency.feeCurrency.shortName)
                        }
                    },
                    style = MaterialTheme.typography.body2,
                )
            }
            if (transaction.isGasFeesAvailable) {
                Spacer(modifier = Modifier.height(8.dp))
                ExpandableGas(
                    transaction = transaction,
                    isExpanded = isExpanded,
                    onExpandClick = { isExpanded = !isExpanded },
                )
            }
            if (transaction.canShowConfirmations) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(id = R.string.sw_confirmations))
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = transaction.confirmations ?: "", style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ExpandableGas(
    transaction: Transaction,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Chip(
            modifier = Modifier
                .height(26.dp)
                .align(Alignment.End),
            onClick = onExpandClick,
            shape = RoundedCornerShape(13.dp),
            colors = ChipDefaults.chipColors(backgroundColor = Color.White),
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(
                    id = if (isExpanded) R.string.sw_click_to_see_less else R.string.sw_click_to_see_more
                ),
                color = SwTheme.colors.primary,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.width(6.dp))
            SwIcon(iconId = if (isExpanded) R.drawable.sw_ic_collapse else R.drawable.sw_ic_expand)
        }
        AnimatedVisibility(visible = isExpanded) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                GasItem(
                    title = stringResource(id = R.string.sw_gas_price),
                    value = "${transaction.gasPrice ?: ""} ${transaction.currency.feeCurrency.shortName}",
                )
                GasItem(
                    title = stringResource(id = R.string.sw_gas_limit),
                    value = transaction.gasLimit ?: "",
                )
                GasItem(
                    title = stringResource(id = R.string.sw_gas_used),
                    value = transaction.gasUsed ?: "",
                )
                GasItem(
                    title = stringResource(id = R.string.sw_confirmations),
                    value = transaction.confirmations ?: "",
                )
            }
        }
    }
}

@Composable
private fun GasItem(title: String, value: String) {
    Row(Modifier.padding(end = 6.dp)) {
        Text(text = "$title:", fontSize = 14.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = value, style = MaterialTheme.typography.body2, fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTransactionTimeFeesCard() {
    SharedWalletTheme {
        TransactionTimeFeesCard(
            transaction = fakeSucceedTransaction,
        )
    }
}
