package com.sharedwallet.sdk.reusable.cards

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.FeePriority
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme
import java.text.DecimalFormat

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GasFeeItem(
    modifier: Modifier = Modifier,
    feeCurrency: CurrencyType,
    isSelected: Boolean,
    gasPrice: Double,
    feePriority: FeePriority,
    onClick: () -> Unit = { },
) {
    Card(
        modifier = modifier,
        backgroundColor = if (isSelected) {
            SwTheme.colors.selectedCard
        } else {
            SwTheme.colors.grayButton.copy(alpha = .5f)
        },
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SwIcon(
                modifier = Modifier.size(30.dp),
                iconId = when (feePriority) {
                    FeePriority.Fast -> R.drawable.sw_ic_priority_fast
                    FeePriority.Medium -> R.drawable.sw_ic_priority_medium
                    FeePriority.Slow -> R.drawable.sw_ic_priority_slow
                },
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(
                    id = when (feePriority) {
                        FeePriority.Fast -> R.string.sw_fast
                        FeePriority.Medium -> R.string.sw_medium
                        FeePriority.Slow -> R.string.sw_slow
                    },
                ),
                style = MaterialTheme.typography.body2,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = MaterialTheme.typography.body2.toSpanStyle(),
                    ) {
                        val formatter = DecimalFormat("#.##########")
                        append(stringResource(id = R.string.sw_max_fee, formatter.format(gasPrice)))
                    }
                    withStyle(
                        style = MaterialTheme.typography.body1.toSpanStyle()
                            .copy(fontSize = 14.sp),
                    ) {
                        append(" ${feeCurrency.shortName}")
                    }
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(14.dp)) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isSelected,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    SwIcon(iconId = R.drawable.sw_ic_priority_selected)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUnselectedGasFeeItem() {
    SharedWalletTheme {
        GasFeeItem(
            feeCurrency = CurrencyType.ETH,
            isSelected = false,
            gasPrice = 0.00000399,
            feePriority = FeePriority.Fast,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSelectedGasFeeItem() {
    SharedWalletTheme {
        GasFeeItem(
            feeCurrency = CurrencyType.ETH,
            isSelected = true,
            gasPrice = 0.00000299,
            feePriority = FeePriority.Medium,
        )
    }
}
