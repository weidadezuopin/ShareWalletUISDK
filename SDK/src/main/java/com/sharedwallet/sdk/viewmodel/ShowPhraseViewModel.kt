package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharedwallet.sdk.data.repository.UserRepository
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.models.SeedWord
import com.sharedwallet.sdk.domain.state.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ShowPhraseViewModel(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
): ViewModel() {

    private val passcode = savedStateHandle.get<String>(Keys.CONFIRMED_CODE)!!

    private val _seedPhraseFlow = MutableStateFlow(listOf<SeedWord>())
    val seedPhraseFlow = _seedPhraseFlow.asStateFlow()

    init {
        viewModelScope.launch {
            when(val request = userRepository.getSeedPhrase(passcode)) {
                is RequestState.Error -> { /* TODO */ }
                is RequestState.Success -> {
                    _seedPhraseFlow.value = request.data
                }
            }
        }
    }
}
