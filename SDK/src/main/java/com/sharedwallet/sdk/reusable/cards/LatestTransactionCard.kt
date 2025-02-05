package com.sharedwallet.sdk.reusable.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.RecordState
import com.sharedwallet.sdk.domain.models.Record
import com.sharedwallet.sdk.domain.utils.isReceived
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.theme.SharedWalletTheme

@Composable
fun LatestTransactionCard(
    record: Record,
    onRecordsClick: () -> Unit = { },
) {
    GrayCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        onClick = onRecordsClick,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SwIcon(iconId = R.drawable.sw_receive_green_circle)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "%s %s %s".format(
                    stringResource(
                        id = when {
                            record.isSent && record.state == RecordState.Failed -> R.string.sw_sent_failed
                            record.isReceived && record.state == RecordState.Failed -> R.string.sw_received_failed
                            record.isSent -> R.string.sw_sent
                            else -> R.string.sw_received
                        },
                    ),
                    record.amount,
                    record.currency.shortName,
                ),
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = record.date,
                maxLines = 1,
            )
        }
    }
}

@Preview
@Composable
fun PreviewLatestTransaction() {
    SharedWalletTheme {
        LatestTransactionCard(
            record = Record(
                id = "1",
                transactionHash = "",
                state = RecordState.Succeed,
                isSent = false,
                currency = CurrencyType.USDT_ERC20,
                amount = "6895.12659437",
                otherAddress = "",
                date = "02-16 15:38",
            )
        )
    }
}
