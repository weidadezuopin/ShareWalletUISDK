package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sharedwallet.sdk.data.mappers.toSeedWord
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.models.SeedWord
import kotlinx.coroutines.flow.*

internal class PhraseConfirmViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val seedPhrase = savedStateHandle.get<String>(Keys.SEED_PHRASE)
        ?.split(',')?.map { it.toSeedWord() }!!

    private val _shuffledSeedPhraseFlow = MutableStateFlow(listOf<SeedWord>())
    val shuffledSeedPhraseFlow = _shuffledSeedPhraseFlow.asStateFlow()

    private val _selectedWordsFlow = MutableStateFlow(listOf<SeedWord>())
    val selectedWordsFlow = _selectedWordsFlow.asStateFlow()

    val isNextEnabledFlow =
        combine(
            flowOf(seedPhrase),
            shuffledSeedPhraseFlow,
            selectedWordsFlow,
        ) { seedPhrase, shuffledPhrase, selectedWords ->
            shuffledPhrase.size == selectedWords.size &&
                    selectedWords.map { it.word } == seedPhrase.map { it.word }
        }

    fun removeWord(word: SeedWord) {
        _selectedWordsFlow.update { it - word }
    }

    fun selectWord(word: SeedWord) {
        _selectedWordsFlow.update { it + word }
    }

    init {
        _shuffledSeedPhraseFlow.update { seedPhrase.shuffled() }
    }
}
