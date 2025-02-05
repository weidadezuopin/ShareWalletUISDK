package com.sharedwallet.sdk.screen.register

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.SwCircleIcon
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.buttons.PrimaryButton
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun PhraseInfoScreen(
    onBackClick: () -> Unit = { },
    onNextClick: () -> Unit = { },
) {
    WalletScaffold(
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_back_up_seed_phrase),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.weight(.5f))
            InfoItem(
                icon = R.drawable.sw_ic_seed_phrase_info,
                title = stringResource(id = R.string.sw_what_is_seed_phrase),
                info = stringResource(id = R.string.sw_seed_phrase_info_text),
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoItem(
                icon = R.drawable.sw_ic_seed_phrase_back_up,
                title = stringResource(id = R.string.sw_backup_the_seed_phrase),
                info = stringResource(id = R.string.sw_seed_phrase_back_up_text),
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoItem(
                icon = R.drawable.sw_ic_seed_phrase_secret,
                title = stringResource(id = R.string.sw_keep_the_mnemonics_in_secret),
                info = stringResource(id = R.string.sw_seed_phrase_secret_text),
            )
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                onClick = onNextClick,
                text = stringResource(id = R.string.sw_start_to_back_up),
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun InfoItem(
    @DrawableRes icon: Int,
    title: String,
    info: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
    ) {
        SwCircleIcon(size = 42.dp, iconId = icon)
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.h2.copy(fontSize = 16.sp),
                color = SwTheme.colors.primary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = info,
                style = MaterialTheme.typography.body2.copy(lineHeight = 18.sp),
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPhraseInfo() {
    SharedWalletTheme {
        PhraseInfoScreen()
    }
}
