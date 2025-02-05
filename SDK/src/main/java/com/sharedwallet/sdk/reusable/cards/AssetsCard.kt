package com.sharedwallet.sdk.reusable.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.utils.logoId
import com.sharedwallet.sdk.domain.utils.primaryColor
import com.sharedwallet.sdk.domain.utils.secondaryColor
import com.sharedwallet.sdk.reusable.CircleBox
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.theme.SharedWalletTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AssetBottomCard(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        backgroundColor = color,
        elevation = 0.dp,
        onClick = onClick ?: { },
        enabled = onClick != null,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopStart,
            content = content,
        )
    }
}

@Composable
fun AssetCurrencyBottomCard(
    modifier: Modifier = Modifier,
    currency: CurrencyType,
    balance: String,
    price: String,
    onClick: (() -> Unit)? = null,
) {
    AssetBottomCard(
        modifier = modifier,
        color = currency.secondaryColor,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                CircleBox(size = 50.dp, color = currency.primaryColor.copy(alpha = .2f)) {
                    SwIcon(modifier = Modifier.size(42.dp), iconId = if (currency == CurrencyType.USDT_ERC20) R.drawable.sw_logo_usdt_erc20_2 else currency.logoId)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = currency.shortName)
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(text = balance)
                Text(text = price, style = MaterialTheme.typography.subtitle2)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCurrencyBottomCard() {
    SharedWalletTheme {
        AssetCurrencyBottomCard(
            currency = CurrencyType.ETH,
            balance = "0.07350900",
            price = "≈¥2.12",
        )
    }
}
