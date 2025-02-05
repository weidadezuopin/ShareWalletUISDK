package com.sharedwallet.sdk.reusable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun SwIcon(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    tint: Color = Color.Unspecified,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = iconId),
        contentDescription = null,
        tint = tint,
    )
}

@Composable
fun SwCircleIcon(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color = Color.White,
    iconTint: Color = Color.Unspecified,
    @DrawableRes iconId: Int,
) {
    Box(modifier = modifier) {
        CircleBox(
            size = size,
            color = color,
        ) {
            SwIcon(
                iconId = iconId,
                tint = iconTint,
            )
        }
    }
}

@Composable
fun BackArrow(modifier: Modifier = Modifier) {
    SwCircleIcon(
        modifier = modifier,
        size = 32.dp,
        iconId = R.drawable.sw_ic_arrow_back,
        color = SwTheme.colors.grayButton,
    )
}
