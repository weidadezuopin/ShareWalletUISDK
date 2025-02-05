package com.sharedwallet.sdk.reusable

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sharedwallet.sdk.theme.SharedWalletTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun WalletScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = { },
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = topBar,
    ) {
        content()
    }
}

@Preview
@Composable
internal fun PreviewColumnScaffold() {
    SharedWalletTheme {
        WalletScaffold(
            topBar = {
                DefaultActionBar(
                    onBackClick = { },
                    title = "Title",
                )
            },
        ) {

        }
    }
}
