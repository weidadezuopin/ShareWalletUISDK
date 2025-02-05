package com.sharedwallet.sdk.reusable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sharedwallet.sdk.theme.SharedWalletTheme

@Composable
fun DefaultActionBar(
    onBackClick: (() -> Unit)? = null,
    title: String = "",
    actions: @Composable () -> Unit = { Spacer(modifier = Modifier) },
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
    ) {
        if (onBackClick != null) {
            BackArrow(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBackClick)
            )
        }
        Text(modifier = Modifier.align(Alignment.Center), text = title)
        Box(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 32.dp)) {
            actions()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActionBar() {
    SharedWalletTheme {
        DefaultActionBar(
            onBackClick = {},
            title = "Title",
        )
    }
}
