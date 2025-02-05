package com.sharedwallet.sdk.screen.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.ErrorText
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.buttons.PrimaryButton
import com.sharedwallet.sdk.reusable.cards.SeedPhraseGridCard
import com.sharedwallet.sdk.reusable.cards.SeedWordItem
import com.sharedwallet.sdk.reusable.dialog.InfoDialog
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.viewmodel.PhraseConfirmViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PhraseConfirmScreen(
    viewModel: PhraseConfirmViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onConfirmed: () -> Unit = { },
) {
    val shuffledSeedPhrase by viewModel.shuffledSeedPhraseFlow.collectAsStateWithLifecycle()
    val selectedWords by viewModel.selectedWordsFlow.collectAsStateWithLifecycle()
    val isNextEnabled by viewModel.isNextEnabledFlow.collectAsStateWithLifecycle(
        initialValue = false
    )

    var showPhraseDirectionDialog by rememberSaveable { mutableStateOf(false) }

    WalletScaffold(
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_confirm),
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
                text = stringResource(id = R.string.sw_select_mnemonic_in_order),
            )
            Spacer(modifier = Modifier.height(40.dp))

            SeedPhraseGridCard(modifier = Modifier.padding(horizontal = 24.dp)) {
                items(selectedWords, key = { it.id }) { seedWord ->
                    SeedWordItem(
                        modifier = Modifier
                            .animateItemPlacement()
                            .padding(horizontal = 2.dp, vertical = 4.dp),
                        text = seedWord.word,
                        isDeleteShow = true,
                        onClick = { viewModel.removeWord(seedWord) },
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(
                visible = selectedWords.size == shuffledSeedPhrase.size && !isNextEnabled,
            ) {
                ErrorText(text = stringResource(id = R.string.sw_phrase_order_wrong))
            }
            Spacer(modifier = Modifier.height(32.dp))
            SeedPhraseGridCard(modifier = Modifier.padding(horizontal = 24.dp)) {
                items(shuffledSeedPhrase, key = { it.id }) { seedWord ->
                    val isEnabled = !selectedWords.contains(seedWord)
                    SeedWordItem(
                        modifier = Modifier
                            .padding(horizontal = 2.dp, vertical = 4.dp),
                        text = seedWord.word,
                        enabled = isEnabled,
                        onClick = { viewModel.selectWord(seedWord) },
                    )
                }
            }
            Spacer(modifier = Modifier.height(72.dp))
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                onClick = { showPhraseDirectionDialog = true },
                text = stringResource(id = R.string.sw_next),
                enabled = isNextEnabled,
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
                onConfirmed()
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPhraseConfirm() {
    SharedWalletTheme {
        PhraseConfirmScreen()
    }
}
