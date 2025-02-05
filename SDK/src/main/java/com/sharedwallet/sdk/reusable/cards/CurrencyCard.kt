package com.sharedwallet.sdk.reusable.cards

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.logoId
import com.sharedwallet.sdk.domain.utils.primaryColor
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.theme.SharedWalletTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CurrencyCard(
    modifier: Modifier = Modifier,
    currency: CurrencyType,
    value: String,
    hasRefresh: Boolean = false,
    loadState: UiState<Unit>? = null,
    onRefreshClick: () -> Unit = { },
) {
    Card(
        modifier = modifier,
        backgroundColor = currency.primaryColor,
        shape = RoundedCornerShape(10.dp),
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SwIcon(modifier = Modifier.size(40.dp), iconId = currency.logoId)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = currency.shortName, fontSize = 18.sp, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = value, color = Color.White)
            if (hasRefresh) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.size(16.dp)) {
                    AnimatedContent(
                        targetState = loadState,
                        transitionSpec = {
                            when (targetState) {
                                is UiState.Success -> fadeIn(tween(400)) with
                                        fadeOut(tween(400))
                                else -> scaleIn(tween(300)) with
                                        scaleOut(tween(300))
                            }
                        }
                    ) { targetState ->
                        when (targetState) {
                            UiState.Loading -> {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                )
                            }
                            is UiState.Success -> {
                                SwIcon(iconId = R.drawable.sw_ic_success_mark)
                            }
                            else -> {
                                IconButton(onClick = onRefreshClick) {
                                    SwIcon(iconId = R.drawable.sw_ic_reload)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCurrencyCard() {
    SharedWalletTheme {
        CurrencyCard(currency = CurrencyType.BTC, value = "1.00000036")
    }
}
