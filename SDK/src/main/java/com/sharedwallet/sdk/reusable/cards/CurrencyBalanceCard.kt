package com.sharedwallet.sdk.reusable.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.sharedwallet.sdk.compose.verticalGradientBackground
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun CurrencyBalanceCard(
    modifier: Modifier = Modifier,
    balance: String,
    isShown: Boolean,
    onVisibilityClick: () -> Unit = { },
    onBuyClick: () -> Unit = { },
) {
    Card(
        modifier = modifier,
        backgroundColor = SwTheme.colors.primaryLight,
        shape = RoundedCornerShape(20.dp),
        elevation = 0.dp,
    ) {
        BalanceCard(
            modifier = Modifier.padding(16.dp),
            balance = balance,
            isShown = isShown,
            onVisibilityClick = onVisibilityClick,
            onBuyClick = onBuyClick,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BalanceCard(
    modifier: Modifier = Modifier,
    balance: String,
    isShown: Boolean,
    onVisibilityClick: () -> Unit = { },
    onBuyClick: () -> Unit = { },
) {
    Card(
        modifier = modifier.height(118.dp),
        backgroundColor = SwTheme.colors.secondary,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width = 1.dp, color = Color.White),
        elevation = 8.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 28.dp, top = 16.dp, end = 28.dp, bottom = 4.dp),
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = balance,
                        style = MaterialTheme.typography.h2,
                        fontSize = 24.sp,
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(top = 6.dp, start = 8.dp)
                            .size(20.dp),
                        onClick = onVisibilityClick,
                    ) {
                        SwIcon(
                            iconId = if (isShown) {
                                R.drawable.sw_visibility_off
                            } else {
                                R.drawable.sw_visibility_on
                            },
                        )
                    }

                }
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier
                        .size(width = 58.dp, height = 24.dp),
                    onClick = onBuyClick,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalGradientBackground(
                                topColor = Color.White,
                                bottomColor = SwTheme.colors.secondary.copy(alpha = .2f),
                            ),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.sw_buy),
                        fontSize = 14.sp,
                        color = SwTheme.colors.secondary,
                    )
                }
            }
            SwIcon(
                modifier = Modifier
                    .width(104.dp)
                    .align(Alignment.BottomEnd),
                iconId = R.drawable.sw_ic_balance_all,
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    SharedWalletTheme {
        CurrencyBalanceCard(
            balance = "¥0.00",
            isShown = false,
        )
    }
}
