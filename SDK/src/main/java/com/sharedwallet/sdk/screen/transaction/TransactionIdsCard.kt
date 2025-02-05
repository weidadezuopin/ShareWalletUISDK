package com.sharedwallet.sdk.screen.transaction

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.compose.clickableNoRipple
import com.sharedwallet.sdk.domain.models.Transaction
import com.sharedwallet.sdk.domain.utils.isReceived
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.temp.fakeSucceedTransaction
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun TransactionIdsCard(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onTransactionIdClick: () -> Unit = { },
    onOtherIdClick: (otherId: String) -> Unit = { },
    onSaveIdClick: (otherId: String) -> Unit = { },
    onCopyClick: (text: String) -> Unit = { },
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            IdItem(
                iconId = R.drawable.sw_ic_transaction_id,
                title = stringResource(id = R.string.sw_tx_id),
                onCopyClick = { onCopyClick(transaction.transactionId) },
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp)
                        .clickableNoRipple(onClick = onTransactionIdClick),
                    text = transaction.transactionId,
                    style = MaterialTheme.typography.body2.copy(textDecoration = TextDecoration.Underline),
                    color = SwTheme.colors.clickableText,
                    fontSize = 12.sp,
                    textAlign = TextAlign.End,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            IdItem(
                iconId = R.drawable.sw_ic_to_id,
                title = stringResource(id = R.string.sw_to),
                onCopyClick = { onCopyClick(transaction.toId) },
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = transaction.toId,
                        style = MaterialTheme.typography.body2,
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                    )
                }
            }
            Box(modifier = Modifier.align(Alignment.End).padding(end = 36.dp)) {
                if (transaction.isReceived) {
                    MyselfAddressText()
                } else {
                    OtherAddress(
                        otherName = transaction.otherName,
                        onSaveClick = { onSaveIdClick(transaction.toId) },
                        onOtherIdClick = { onOtherIdClick(transaction.toId) },
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            IdItem(
                iconId = R.drawable.sw_ic_from_id,
                title = stringResource(id = R.string.sw_from),
                onCopyClick = { onCopyClick(transaction.fromId) },
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 4.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = transaction.fromId,
                        style = MaterialTheme.typography.body2,
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                    )
                }
            }
            Box(modifier = Modifier.align(Alignment.End).padding(end = 36.dp)) {
                if (transaction.isSent) {
                    MyselfAddressText()
                } else {
                    OtherAddress(
                        otherName = transaction.otherName,
                        onSaveClick = { onSaveIdClick(transaction.fromId) },
                        onOtherIdClick = { onOtherIdClick(transaction.fromId) },
                    )
                }
            }
        }

    }
}

@Composable
private fun IdItem(
    @DrawableRes iconId: Int,
    title: String,
    onCopyClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.Top,
    ) {
        SwIcon(iconId = iconId)
        Spacer(modifier = Modifier.width(8.dp))
        Text(modifier = Modifier.padding(top = 4.dp), text = title)
        Spacer(modifier = Modifier.width(12.dp))
        content()
        Spacer(modifier = Modifier.width(6.dp))
        IconButton(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(26.dp),
            onClick = onCopyClick,
        ) {
            SwIcon(iconId = R.drawable.sw_ic_copy)
        }
    }
}

@Composable
private fun MyselfAddressText() {
    Text(
        text = stringResource(id = R.string.sw_myself_address),
        style = MaterialTheme.typography.body2,
        color = SwTheme.colors.clickableText,
        fontSize = 12.sp,
    )
}

@Composable
private fun OtherAddress(
    otherName: String?,
    onSaveClick: () -> Unit,
    onOtherIdClick: () -> Unit,
) {
    if (otherName != null) {
        Text(
            modifier = Modifier.clickableNoRipple(onClick = onOtherIdClick),
            text = stringResource(id = R.string.sw_other_name, otherName),
            style = MaterialTheme.typography.body2,
            color = SwTheme.colors.clickableText,
            fontSize = 12.sp,
        )
    } else {
        Button(
            modifier = Modifier.height(28.dp),
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = SwTheme.colors.clickableText.copy(alpha = .2f),
                contentColor = SwTheme.colors.clickableText,
            ),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
        ) {
            Text(
                text = stringResource(id = R.string.sw_save_to_my_address_book),
                style = MaterialTheme.typography.body1,
                fontSize = 12.sp,
            )
        }
    }
}

@Preview
@Composable
fun PreviewTransactionIdsCard() {
    SharedWalletTheme {
        TransactionIdsCard(transaction = fakeSucceedTransaction)
    }
}
