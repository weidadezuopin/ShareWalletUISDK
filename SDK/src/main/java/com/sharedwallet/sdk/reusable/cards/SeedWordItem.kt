package com.sharedwallet.sdk.reusable.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.theme.SharedWalletTheme

@Composable
fun SeedWordItem(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    isDeleteShow: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Card(
        modifier = modifier
            .alpha(if (enabled) 1f else .2f)
            .clip(RoundedCornerShape(4.dp))
            .clickable(
                enabled = onClick != null && enabled,
                onClick = { onClick?.invoke() },
            ),
        backgroundColor = Color.White,
    ) {
        Box(
            modifier = Modifier.height(44.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = text,
                fontSize = 14.sp,
            )
            if (isDeleteShow) {
                SwIcon(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .align(Alignment.TopEnd),
                    iconId = R.drawable.sw_ic_delete_seed,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSeedWord() {
    SharedWalletTheme {
        SeedWordItem(
            modifier = Modifier.width(80.dp),
            text = "Test",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSeedWordDisabled() {
    SharedWalletTheme {
        SeedWordItem(
            modifier = Modifier.width(80.dp),
            text = "Test",
            enabled = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSeedWordDelete() {
    SharedWalletTheme {
        SeedWordItem(
            modifier = Modifier.width(80.dp),
            text = "Test",
            isDeleteShow = true,
        )
    }
}
