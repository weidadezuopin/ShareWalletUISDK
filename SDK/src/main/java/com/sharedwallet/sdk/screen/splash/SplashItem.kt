package com.sharedwallet.sdk.screen.splash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.reusable.SwCircleIcon
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun SplashItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = SwTheme.colors.primary,
                fontSize = 20.sp,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.body2,
            )
        }
        SwCircleIcon(
            size = 28.dp,
            iconId = R.drawable.sw_ic_arrow_forward,
            color = SwTheme.colors.primary.copy(alpha = .2f),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashItem() {
    SharedWalletTheme {
        SplashItem(title = "Some title", subtitle = "Something about this title")
    }
}
