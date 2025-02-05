package com.sharedwallet.sdk.screen.transfer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.state.FieldError
import com.sharedwallet.sdk.domain.utils.logoId
import com.sharedwallet.sdk.reusable.ErrorText
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.reusable.field.SwTextField
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
internal fun TransferAmountCard(
    modifier: Modifier = Modifier,
    currency: CurrencyType,
    amount: String,
    amountEqualTo: String,
    amountError: FieldError? = null,
    allBalance: String,
    onAmountChanged: (amount: String) -> Unit,
    onAllClick: () -> Unit = { },
) {
    Card(
        modifier = modifier,
        backgroundColor = SwTheme.colors.blueCard,
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SwIcon(modifier = Modifier.size(36.dp), iconId = currency.logoId)
                SwTextField(
                    modifier = Modifier.weight(1f),
                    value = amount,
                    onValueChange = onAmountChanged,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    placeholder = {
                        Text(
                            modifier = Modifier.padding(horizontal = 2.dp),
                            text = "0.00000000",
                            maxLines = 1,
                            color = SwTheme.colors.placeholder,
                        )
                    },
                )
                Text(
                    text = "≈",
                    style = MaterialTheme.typography.h2,
                    fontSize = 28.sp,
                    maxLines = 1,
                )
                Text(
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    text = amountEqualTo,
                    textAlign = TextAlign.End,
                    color = SwTheme.colors.grayCurrency,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(visible = amountError != null) {
                ErrorText(text = when(amountError) {
                    is FieldError.Empty -> stringResource(id = R.string.sw_amount_empty_message)
                     is FieldError.Invalid -> when (amountError.reason) {
                             FieldError.REASON_BALANCE_NOT_ENOUGH ->
                                 stringResource(id = R.string.sw_balance_not_enough)
                             else -> stringResource(id = R.string.sw_amount_invalid)
                         }
                    null -> ""
                })
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = Color.White,
                    elevation = 0.dp,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.body2.toSpanStyle()
                                    .copy(fontSize = 12.sp),
                            ) {
                                append(stringResource(id = R.string.sw_balance_is, allBalance))
                            }
                            withStyle(
                                style = MaterialTheme.typography.body1.toSpanStyle()
                                    .copy(fontSize = 12.sp),
                            ) {
                                append(" ${currency.shortName}")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = modifier
                        .height(36.dp)
                        .width(80.dp),
                    onClick = onAllClick,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledBackgroundColor = SwTheme.colors.primary.copy(alpha = .3f),
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.sw_all),
                        style = MaterialTheme.typography.body2,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTransferAmount() {
    SharedWalletTheme {
        TransferAmountCard(
            currency = CurrencyType.ETH,
            amount = "0.00000000",
            amountEqualTo = "¥0.00",
            allBalance = "3.34216876",
            onAmountChanged = {},
        )
    }
}
