package com.sharedwallet.sdk.screen.transfer

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.state.FeeRefreshState
import com.sharedwallet.sdk.domain.utils.feeCurrency
import com.sharedwallet.sdk.reusable.SwCircleIcon
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.reusable.isChineseLang
import com.sharedwallet.sdk.reusable.progress.SmallLoadingProgress
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MinerFeeCard(
    modifier: Modifier = Modifier,
    currency: CurrencyType,
    fee: String,
    feeEqualTo: String,
    feeRefreshState: FeeRefreshState,
    clickable: Boolean = true,
    onClick: () -> Unit = { },
    onInfoClick: () -> Unit = { },
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.sw_miners_fee))
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                modifier = Modifier.size(20.dp),
                onClick = onInfoClick,
            ) {
                SwIcon(iconId = R.drawable.sw_ic_miner_fee_info,)
            }
            Spacer(modifier = Modifier.weight(1f))
            if (feeRefreshState is FeeRefreshState.CountDown) {
                Row {
                    if (!isChineseLang) {
                        Text(text = stringResource(id = R.string.sw_refresh_in))
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    AnimatedContent(
                        targetState = feeRefreshState.sec,
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInVertically { height -> height } + fadeIn() with
                                        slideOutVertically { height -> -height } + fadeOut()
                            } else {
                                slideInVertically { height -> -height } + fadeIn() with
                                        slideOutVertically { height -> height } + fadeOut()
                            }.using(
                                SizeTransform(clip = false)
                            )
                        }
                    ) { targetCount ->
                        Text(text = "$targetCount")
                    }
                    if (isChineseLang) {
                        Text(text = stringResource(id = R.string.sw_refresh_in))
                    }
                }
            }
            AnimatedVisibility(visible = feeRefreshState is FeeRefreshState.Loading) {
                SmallLoadingProgress()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            backgroundColor = SwTheme.colors.blueCard,
            shape = RoundedCornerShape(12.dp),
            onClick = onClick,
            enabled = clickable && feeRefreshState !is FeeRefreshState.Loading,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.body2.toSpanStyle()
                                    .copy(fontSize = 16.sp),
                            ) {
                                append("${stringResource(id = R.string.sw_estimated_fee)}: ")
                            }
                            withStyle(
                                style = MaterialTheme.typography.body1.toSpanStyle(),
                            ) {
                                append(feeEqualTo)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.body2.toSpanStyle()
                                    .copy(fontSize = 14.sp),
                            ) {
                                append(fee)
                            }
                            withStyle(
                                style = MaterialTheme.typography.h2.toSpanStyle()
                                    .copy(fontSize = 14.sp),
                            ) {
                                append(" ${currency.feeCurrency.shortName}")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (clickable) {
                    SwCircleIcon(
                        size = 32.dp,
                        iconId = R.drawable.sw_ic_arrow_forward,
                        color = SwTheme.colors.primary,
                        iconTint = Color.White,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMinerFee() {
    SharedWalletTheme {
        MinerFeeCard(
            currency = CurrencyType.ETH,
            fee = "0.00000120",
            feeEqualTo = "¥ 0.038",
            feeRefreshState = FeeRefreshState.Loading,
        )
    }
}
