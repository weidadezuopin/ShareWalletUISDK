package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharedwallet.sdk.data.repository.CheckPasscodeRepository
import com.sharedwallet.sdk.domain.models.PassCode
import com.sharedwallet.sdk.domain.state.PasscodeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class EnterPasscodeViewModel(
    private val checkPasscodeRepository: CheckPasscodeRepository,
) : ViewModel() {

    private val _passCodeFlow = MutableStateFlow(PassCode())
    val passCodeFlow = _passCodeFlow.asStateFlow()

    private val _isLoadingFlow = MutableStateFlow(false)
    val isLoadingFlow = _isLoadingFlow.asStateFlow()

    private val _passcodeStateFlow = MutableStateFlow<PasscodeState?>(null)
    val passcodeStateFlow = _passcodeStateFlow.asStateFlow()

    fun setCode(passCode: PassCode) {
        _passCodeFlow.value = passCode
    }

    private fun confirmPasscode(passCode: PassCode) {
        viewModelScope.launch {
            _isLoadingFlow.value = true
            val isCorrect = checkPasscodeRepository.check(passCode.code)
            _passcodeStateFlow.value = if (isCorrect) {
                PasscodeState.Confirmed(passCode)
            } else {
                PasscodeState.Error
            }
            _isLoadingFlow.value = false
        }
    }

    init {
        viewModelScope.launch {
            passCodeFlow.collect { passCode ->
                _passcodeStateFlow.value = null
                if (passCode.isFilled) {
                    confirmPasscode(passCode)
                }
            }
        }
    }
}
