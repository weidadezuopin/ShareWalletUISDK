package com.sharedwallet.sdk.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.horizontalGradientBackground(
    startColor: Color,
    endColor: Color,
) = background(
    brush = Brush.horizontalGradient(
        colors = listOf(
            startColor,
            endColor,
        )
    )
)

fun Modifier.verticalGradientBackground(
    topColor: Color,
    bottomColor: Color,
) = background(
    brush = Brush.verticalGradient(
        colors = listOf(
            topColor,
            bottomColor,
        )
    )
)

fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick,
    )
}
