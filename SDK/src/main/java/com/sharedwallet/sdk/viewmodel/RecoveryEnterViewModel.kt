package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharedwallet.sdk.data.mappers.toSeedWord
import com.sharedwallet.sdk.data.repository.RecoverPhraseRepository
import com.sharedwallet.sdk.data.repository.ToastRepository
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.models.SeedWord
import com.sharedwallet.sdk.domain.state.RequestState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

@OptIn(FlowPreview::class)
internal class RecoveryEnterViewModel(
    savedStateHandle: SavedStateHandle,
    private val toastRepository: ToastRepository,
    private val recoverPhraseRepository: RecoverPhraseRepository,
) : ViewModel() {

    private val passcode = savedStateHandle.get<String>(Keys.CONFIRMED_CODE)!!

    private val _selectedWordsFlow = MutableStateFlow(listOf<SeedWord>())
    val selectedWordsFlow = _selectedWordsFlow.asStateFlow()

    private val _isLoadingFlow = MutableStateFlow(false)
    val isLoadingFlow = _isLoadingFlow.asStateFlow()

    private val _isRecoveredFlow = MutableStateFlow(false)
    val isRecoveredFlow = _isRecoveredFlow.asStateFlow()

    val wordTextFlow = MutableStateFlow("")

    val suggestedWordsFlow = wordTextFlow.debounce { query ->
        if (query.isEmpty()) 0 else 50
    }.map { query ->
        if (query.isEmpty()) {
            listOf()
        } else {
            recoverPhraseRepository.fetchSeedWord(query)
        }
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = listOf(),
            started = SharingStarted.WhileSubscribed(5_000),
        )

    val isPhraseFilledFlow = selectedWordsFlow.map { it.size == 12 }

    fun tryToast(message: String) = toastRepository.tryToast(message)

    fun removeWord(word: SeedWord) {
        _selectedWordsFlow.update { it - word }
    }

    fun selectWord(word: String): Boolean {
        val canBeAdded = suggestedWordsFlow.value.contains(word)
        if (canBeAdded) {
            wordTextFlow.update { "" }
            _selectedWordsFlow.update { it + word.toSeedWord() }
        }
        return canBeAdded
    }

    init {
        viewModelScope.launch {
            selectedWordsFlow.collect { list ->
                if (list.size == 12) {
                    _isLoadingFlow.value = true
                    _isRecoveredFlow.value =
                        when (recoverPhraseRepository.recoverUserAccount(passcode, list)) {
                            is RequestState.Success -> true
                            is RequestState.Error -> false
                        }
                    _isLoadingFlow.value = false
                }
            }
        }
    }
}
