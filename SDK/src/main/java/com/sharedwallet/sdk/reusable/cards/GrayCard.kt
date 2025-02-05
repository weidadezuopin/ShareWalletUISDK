package com.sharedwallet.sdk.reusable.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sharedwallet.sdk.compose.verticalGradientBackground
import com.sharedwallet.sdk.theme.SwTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GrayCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    elevation: Dp = 8.dp,
    shape: Shape = RoundedCornerShape(10.dp),
    border: BorderStroke? = BorderStroke(width = 2.dp, color = Color.White),
    content: @Composable BoxScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        border = border,
        elevation = elevation,
        onClick = onClick ?: { },
        enabled = onClick != null,
    ) {
        Box (
            modifier = Modifier
                .verticalGradientBackground(
                    topColor = SwTheme.colors.grayCard,
                    bottomColor = Color.White,
                ),
            content = content,
        )
    }
}

@Preview
@Composable
fun PreviewGrayCard() {
    GrayCard {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Hello!",
        )
    }
}
