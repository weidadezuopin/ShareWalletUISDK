package com.sharedwallet.sdk.screen.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.models.SeedWord
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.PreventScreenshot
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.buttons.PrimaryButton
import com.sharedwallet.sdk.reusable.cards.SeedPhraseGridCard
import com.sharedwallet.sdk.reusable.cards.SeedWordItem
import com.sharedwallet.sdk.reusable.dialog.InfoDialog
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.viewmodel.PhraseGenerateViewModel

@Composable
internal fun PhraseBackupScreen(
    viewModel: PhraseGenerateViewModel = koinSwViewModel(),
    onNotNowClick: () -> Unit = { },
    onConfirmClick: (seedPhrase: List<SeedWord>) -> Unit = { },
) {
    PreventScreenshot()
    BackHandler {
        // Do nothing
    }

    val seedPhrase by viewModel.seedPhraseFlow.collectAsStateWithLifecycle()

    var showPhraseDirectionDialog by rememberSaveable { mutableStateOf(false) }

    WalletScaffold(
        topBar = {
            DefaultActionBar(title = stringResource(id = R.string.sw_back_up_mnemonic_phrase))
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
            Spacer(modifier = Modifier.height(80.dp))
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
            Spacer(modifier = Modifier.height(12.dp))
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { showPhraseDirectionDialog = true }) {
                Text(
                    text = stringResource(id = R.string.sw_not),
                    style = MaterialTheme.typography.body2,
                    fontSize = 16.sp,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                onClick = { onConfirmClick(seedPhrase) },
                text = stringResource(id = R.string.sw_confirmed_backup),
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (showPhraseDirectionDialog) {
        InfoDialog(
            text = {
                Text(
                    text = stringResource(id = R.string.sw_phrase_direction_note),
                    textAlign = TextAlign.Center,
                )
            },
            onDismissRequest = { showPhraseDirectionDialog = false },
            actionText = stringResource(id = R.string.sw_got_it),
            onActionClick = {
                showPhraseDirectionDialog = false
                onNotNowClick()
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSeedPhraseShow() {
    SharedWalletTheme {
        PhraseBackupScreen()
    }
}
