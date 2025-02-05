package com.sharedwallet.sdk.screen.recovery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.cards.SeedPhraseGridCard
import com.sharedwallet.sdk.reusable.cards.SeedWordItem
import com.sharedwallet.sdk.reusable.field.SwTextField
import com.sharedwallet.sdk.reusable.progress.SmallLoadingProgress
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme
import com.sharedwallet.sdk.viewmodel.RecoveryEnterViewModel

@OptIn(ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
internal fun EnterSeedPhraseScreen(
    viewModel: RecoveryEnterViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onRecovery: () -> Unit = { },
) {
    val context = LocalContext.current
    val selectedWords by viewModel.selectedWordsFlow.collectAsStateWithLifecycle()
    val suggestedWords by viewModel.suggestedWordsFlow.collectAsStateWithLifecycle(
        initialValue = listOf(),
    )
    val wordTextField by viewModel.wordTextFlow.collectAsStateWithLifecycle()
    val isPhraseFilled by viewModel.isPhraseFilledFlow.collectAsStateWithLifecycle(
        initialValue = false
    )
    val isLoading by viewModel.isLoadingFlow.collectAsStateWithLifecycle()
    val isRecovered by viewModel.isRecoveredFlow.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = isRecovered) {
        if (isRecovered) {
            onRecovery()
        }
    }

    fun onWordSelected(word: String) {
        val isAdded = viewModel.selectWord(word)
        if (!isAdded) {
            viewModel.tryToast(context.getString(R.string.sw_invalid))
        }
    }

    WalletScaffold(
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_enter_phrase),
            )
        },
    ) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(62.dp))
                SeedPhraseGridCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    items(selectedWords, key = { it.id }) { seedWord ->
                        SeedWordItem(
                            modifier = Modifier
                                .animateItemPlacement()
                                .padding(horizontal = 2.dp, vertical = 4.dp),
                            text = seedWord.word,
                            isDeleteShow = true,
                            onClick = { if(!isLoading) viewModel.removeWord(seedWord) },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(72.dp))
            }
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                visible = isLoading,
            ) {
                SmallLoadingProgress()
            }
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(visible = suggestedWords.isNotEmpty() && !isPhraseFilled) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    if (suggestedWords.isEmpty()) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            backgroundColor = SwTheme.colors.blueCard,
                            elevation = 0.dp,
                            shape = RoundedCornerShape(12.dp),
                        ) {}
                    }
                    suggestedWords.forEachIndexed { index, word ->
                        Chip(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            onClick = { onWordSelected(word) },
                            colors = ChipDefaults.chipColors(backgroundColor = SwTheme.colors.blueCard),
                            shape = when {
                                suggestedWords.size == 1 -> RoundedCornerShape(12.dp)
                                index == 0 -> RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                                index == suggestedWords.size - 1 -> RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                                else -> RectangleShape
                            },
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = word,
                                style = MaterialTheme.typography.body2,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                SwTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .weight(1f)
                        .height(48.dp),
                    value = wordTextField,
                    singleLine = true,
                    length = 10,
                    placeholder = {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = stringResource(id = R.string.sw_please_input_seed_phrase),
                            color = SwTheme.colors.placeholder
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = SwTheme.colors.grayButton),
                    enabled = !isPhraseFilled,
                    onValueChange = { query ->
                        query.trim().let {
                            viewModel.wordTextFlow.value = it
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { onWordSelected(wordTextField) }),
                )
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    modifier = Modifier.height(48.dp),
                    onClick = { onWordSelected(wordTextField) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = SwTheme.colors.primary),
                    enabled = !isLoading,
                ) {
                    Text(text = stringResource(id = R.string.sw_next))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewRecoveryEnter() {
    SharedWalletTheme {
        EnterSeedPhraseScreen()
    }
}
