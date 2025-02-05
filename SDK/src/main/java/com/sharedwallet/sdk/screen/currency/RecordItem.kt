package com.sharedwallet.sdk.screen.currency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.models.Record
import com.sharedwallet.sdk.domain.utils.color
import com.sharedwallet.sdk.domain.utils.plusOrMinus
import com.sharedwallet.sdk.domain.utils.sendStateDrawableRes
import com.sharedwallet.sdk.domain.utils.stringRes
import com.sharedwallet.sdk.reusable.SwCircleIcon
import com.sharedwallet.sdk.temp.fakeRecords
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RecordItem(
    record: Record,
    onClick: () -> Unit = { },
) {
    Card(
        modifier = Modifier.padding(4.dp),
        backgroundColor = SwTheme.colors.grayCard,
        shape = RoundedCornerShape(10.dp),
        elevation = 0.dp,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SwCircleIcon(
                size = 36.dp,
                iconId = record.sendStateDrawableRes,
                color = if (record.isSent) SwTheme.colors.failed.copy(alpha = .1f) else
                    SwTheme.colors.succeed.copy(alpha = .1f),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(id = if (record.isSent) R.string.sw_out else R.string.sw_in),
                    style = MaterialTheme.typography.body2,
                    fontSize = 16.sp,
                    color = if (record.isSent) SwTheme.colors.failed else SwTheme.colors.succeed,
                )
                Text(
                    text = "${record.otherAddress.take(3)}...${record.otherAddress.takeLast(4)}",
                    style = MaterialTheme.typography.body2,
                    fontSize = 12.sp,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(top = 4.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = "${record.plusOrMinus}${record.amount}",
                    style = MaterialTheme.typography.body2,
                    fontSize = 12.sp,
                )
                if (record.isSent) {
                    Text(
                        text = stringResource(id = record.state.stringRes),
                        style = MaterialTheme.typography.body2,
                        fontSize = 12.sp,
                        color = record.state.color(),
                    )
                } else {
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecordItem() {
    SharedWalletTheme {
        RecordItem(record = fakeRecords.first())
    }
}
