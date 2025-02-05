package com.sharedwallet.sdk.screen.receive

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.buttons.PrimaryButton
import com.sharedwallet.sdk.theme.SharedWalletTheme

@Composable
internal fun PrivateKeyWarningScreen(
    onNextClick: () -> Unit = { },
) {

    WalletScaffold(
        topBar = {
            DefaultActionBar(
                title = stringResource(id = R.string.sw_private_key),
            )
        },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            SwIcon(modifier = Modifier.size(138.dp), iconId = R.drawable.sw_ic_avoid_screenshot)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(horizontal = 48.dp),
                text = stringResource(id = R.string.sw_privateKey_security_warning),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                onClick = onNextClick,
                text = stringResource(id = R.string.sw_show),
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPhraseWarning() {
    SharedWalletTheme {
        PrivateKeyWarningScreen()
    }
}
