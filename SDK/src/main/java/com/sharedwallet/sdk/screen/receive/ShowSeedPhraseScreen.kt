package com.sharedwallet.sdk.screen.receive

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.PreventScreenshot
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.cards.SeedPhraseGridCard
import com.sharedwallet.sdk.reusable.cards.SeedWordItem
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.viewmodel.ShowPhraseViewModel

@Composable
internal fun ShowSeedPhraseScreen(
    viewModel: ShowPhraseViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
) {
    PreventScreenshot()

    val seedPhrase by viewModel.seedPhraseFlow.collectAsStateWithLifecycle()

    WalletScaffold(
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_back_up_mnemonic_phrase),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = stringResource(id = R.string.sw_mnemonic_write_note),
            )
            Spacer(modifier = Modifier.height(32.dp))
            SeedPhraseGridCard(modifier = Modifier.padding(horizontal = 24.dp)) {
                itemsIndexed(seedPhrase) { index, seedWord ->
                    SeedWordItem(
                        modifier = Modifier.padding(horizontal = 2.dp, vertical = 4.dp),
                        text = "${index + 1}.${seedWord.word}",
                    )
                }
            }
            Spacer(modifier = Modifier.height(92.dp))
            Text(
                modifier = Modifier.padding(horizontal = 44.dp),
                text = stringResource(id = R.string.sw_keep_mnemonic_in_safe_note),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                modifier = Modifier.padding(horizontal = 44.dp),
                text = stringResource(id = R.string.sw_share_and_store_note),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewShowSeedPhrase() {
    SharedWalletTheme {
        ShowSeedPhraseScreen()
    }
}
