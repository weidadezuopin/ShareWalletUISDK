package com.sharedwallet.sdk.screen.receive

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.PrivateKeyFormat
import com.sharedwallet.sdk.domain.utils.copyToClipboard
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.domain.utils.stringRes
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.PreventScreenshot
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.buttons.PrimaryButton
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme
import com.sharedwallet.sdk.viewmodel.PrivateKeyViewModel

@Composable
internal fun PrivateKeyScreen(
    viewModel: PrivateKeyViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
) {
    val context = LocalContext.current
    val privateKey by viewModel.privateKeyFlow.collectAsStateWithLifecycle("")

    val scaffoldState = rememberScaffoldState()

    PreventScreenshot()

    WalletScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_display_private_key),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                backgroundColor = SwTheme.colors.blueCard,
                shape = RoundedCornerShape(12.dp),
                elevation = 0.dp,
            ) {
                FormatItem(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Text(text = "${stringResource(id = R.string.sw_private_key)}:")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = privateKey,
                        style = MaterialTheme.typography.body2,
                        fontSize = 16.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                onClick = {
                    context.copyToClipboard(privateKey)
                    viewModel.tryToast(context.getString(R.string.sw_copied))
                },
                text = stringResource(id = R.string.sw_copy),
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun FormatItem(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = PrivateKeyFormat.Hex.stringRes),
                fontSize = 20.sp,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPrivateKey() {
    SharedWalletTheme {
        PrivateKeyScreen()
    }
}
