package com.sharedwallet.sdk.reusable.field

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.compose.verticalGradientBackground
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.utils.logoId
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun CurrencyAddressField(
    modifier: Modifier = Modifier,
    currency: CurrencyType,
    value: String,
    onValueChange: (String) -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
    ) {
        Box (
            modifier = Modifier
                .verticalGradientBackground(
                    topColor = SwTheme.colors.grayCard,
                    bottomColor = Color.White,
                ),
        ) {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SwTextField(
                    modifier = Modifier.weight(1f),
                    value = value,
                    onValueChange = onValueChange,
                    length = 42,
                    singleLine = true,
                    contentPadding = PaddingValues(horizontal = 0.dp, vertical = 8.dp),
                    placeholder = {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = stringResource(
                                id = R.string.sw_enter_currency_address,
                                currency.shortName,
                            ),
                            style = MaterialTheme.typography.body2,
                            fontSize = 16.sp,
                            color = SwTheme.colors.placeholder,
                        )
                    },
                )
                Spacer(modifier = Modifier.width(8.dp))
                SwIcon(modifier = Modifier.size(36.dp), iconId = currency.logoId)
            }
        }
    }
}

@Preview
@Composable
fun PreviewCurrencyAddress() {
    SharedWalletTheme {
        CurrencyAddressField(currency = CurrencyType.ETH, value = "", onValueChange = {})
    }
}
