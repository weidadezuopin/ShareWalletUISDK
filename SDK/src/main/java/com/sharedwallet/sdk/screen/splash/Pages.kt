package com.sharedwallet.sdk.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun FirstSplashPage() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = buildAnnotatedString {
                val text = stringResource(id = R.string.sw_splash_multi_Chain_wallet)
                pushStyle(MaterialTheme.typography.body2.toSpanStyle()
                    .copy(fontSize = 24.sp))
                append(text)
                val wallet = stringResource(id = R.string.sw_wallet)
                val startIndex = text.indexOf(wallet)
                addStyle(
                    style = MaterialTheme.typography.h2.toSpanStyle()
                        .copy(color = SwTheme.colors.primary, fontSize = 24.sp),
                    start = startIndex,
                    end = startIndex + wallet.length,
                )
            },
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.weight(1.5f))
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * .3f),
            painter = painterResource(R.drawable.sw_image_first_splash),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun SecondSplashPage() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = buildAnnotatedString {
                val text = stringResource(id = R.string.sw_splash_your_wallet_protected)
                pushStyle(MaterialTheme.typography.body2.toSpanStyle()
                    .copy(fontSize = 24.sp))
                append(text)
                val wallet = stringResource(id = R.string.sw_wallet)
                val startIndex = text.indexOf(wallet)
                addStyle(
                    style = MaterialTheme.typography.h2.toSpanStyle()
                        .copy(color = SwTheme.colors.primary, fontSize = 24.sp),
                    start = startIndex,
                    end = startIndex + wallet.length,
                )
            },
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.weight(1.5f))
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * .3f),
            painter = painterResource(R.drawable.sw_image_second_splash),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFirstPage() {
    SharedWalletTheme {
        Box(modifier = Modifier.height(360.dp)) {
            FirstSplashPage()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSecondPage() {
    SharedWalletTheme {
        Box(modifier = Modifier.height(360.dp)) {
            SecondSplashPage()
        }
    }
}
