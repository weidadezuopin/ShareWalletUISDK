package com.sharedwallet.sdk.reusable.progress

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.theme.SharedWalletTheme

@Composable
fun SmallLoadingProgress(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition()

    val angle by transition.animateValue(
        0f,
        360f,
        Float.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1_000,
                easing = LinearEasing
            )
        )
    )

    SwIcon(
        modifier = modifier.rotate(angle),
        iconId = R.drawable.sw_ic_small_progress,
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCircularProgress() {
    SharedWalletTheme {
        SmallLoadingProgress()
    }
}
