package com.sharedwallet.sdk.screen.transfer

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.utils.feeCurrency
import com.sharedwallet.sdk.domain.utils.hasMinerFee
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.reusable.buttons.PrimaryButton
import com.sharedwallet.sdk.reusable.cards.CurrencyCard
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun PaymentDetailsBottomSheet(
    currency: CurrencyType,
    toAddressId: String,
    fromAddressId: String,
    amount: String,
    minerFee: String,
    minerFeeEquation: String,
    onCloseClick: () -> Unit = { },
    onNextClick: () -> Unit = { },
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(30.dp),
            onClick = onCloseClick,
        ) {
            SwIcon(
                iconId = R.drawable.sw_ic_close_sheet,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 28.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(id = R.string.sw_payment_details))
            Spacer(modifier = Modifier.height(20.dp))
            CurrencyCard(currency = currency, value = amount)
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
            ) {
                Column(modifier = Modifier.padding(horizontal = 28.dp, vertical = 20.dp)) {
                    Item(
                        iconId = R.drawable.sw_ic_payment_info,
                        title = stringResource(id = R.string.sw_payment_info),
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.sw_currency_transfer,
                                currency.shortName,
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Item(
                        iconId = R.drawable.sw_ic_to_id,
                        title = stringResource(id = R.string.sw_to),
                    ) {
                        Text(
                            text = toAddressId,
                            style = MaterialTheme.typography.body2,
                            fontSize = 12.sp,
                            textAlign = TextAlign.End,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Item(
                        iconId = R.drawable.sw_ic_from_id,
                        title = stringResource(id = R.string.sw_from),
                    ) {
                        Text(
                            text = fromAddressId,
                            style = MaterialTheme.typography.body2,
                            fontSize = 12.sp,
                            textAlign = TextAlign.End,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (currency.hasMinerFee) {
                        Item(
                            iconId = R.drawable.sw_ic_payment_miner_fee,
                            title = stringResource(id = R.string.sw_miners_fee),
                        ) {
                            Card(
                                elevation = 0.dp,
                                backgroundColor = SwTheme.colors.secondary.copy(alpha = .1f),
                                contentColor = SwTheme.colors.secondary,
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 4.dp,
                                    ),
                                    text = "$minerFee${currency.feeCurrency.shortName}",
                                    fontSize = 14.sp,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            modifier = Modifier.align(Alignment.End),
                            text = minerFeeEquation,
                            style = MaterialTheme.typography.body2,
                            fontSize = 12.sp,
                        )
                    } else {
                        Card(
                            backgroundColor = Color.Red.copy(alpha = .1f),
                            contentColor = Color.Red,
                            shape = RoundedCornerShape(6.dp),
                            elevation = 0.dp,
                        ) {
                            Text(
                                modifier = Modifier.padding(12.dp),
                                text = stringResource(id = R.string.sw_trx_bandwidth_warning),
                                style = MaterialTheme.typography.body2,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNextClick,
                text = stringResource(id = R.string.sw_next),
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun Item(
    @DrawableRes iconId: Int,
    title: String,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SwIcon(iconId = iconId)
        Spacer(modifier = Modifier.width(8.dp))
        Text(modifier = Modifier.widthIn(min = 72.dp), text = title)
        Spacer(modifier = Modifier.weight(1f))
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTransferBottomSheet() {
    SharedWalletTheme {
        PaymentDetailsBottomSheet(
            currency = CurrencyType.TRX,
            toAddressId = "TKZKaV4sN5cFKwwGPtEmfUphfGzEML1X19",
            fromAddressId = "TKZKaV4sN5cFKwwGPtEmfUphfGzEML1X19",
            amount = "1.00000036",
            minerFee = "0.000714",
            minerFeeEquation = "≈Gas(70000)*Gas Price(12 gwei)",
        )
    }
}
