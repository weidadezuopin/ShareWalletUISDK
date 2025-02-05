package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharedwallet.sdk.domain.enums.SetPasscodeState
import com.sharedwallet.sdk.domain.models.PassCode
import com.sharedwallet.sdk.domain.state.PasscodeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class SetPasscodeViewModel : ViewModel() {

    private val createdPassCodeFlow = MutableStateFlow<PassCode?>(null)

    private val _passCodeFlow = MutableStateFlow(PassCode())
    val passCodeUiFlow = _passCodeFlow.asStateFlow()

    private val _setPasscodeStateFlow = MutableStateFlow(SetPasscodeState.Create)
    val setPasscodeStateFlow = _setPasscodeStateFlow.asStateFlow()

    private val _passcodeStateFlow = MutableStateFlow<PasscodeState?>(null)
    val passcodeStateFlow = _passcodeStateFlow.asStateFlow()

    fun setCode(passCode: PassCode) {
        _passCodeFlow.update { passCode }
    }

    private fun moveToConfirm(passCode: PassCode) {
        createdPassCodeFlow.update { passCode }
        _passCodeFlow.update { PassCode() }
        _setPasscodeStateFlow.update { SetPasscodeState.Confirm }
    }

    private fun setPasscode(passCode: PassCode) {
        if (passCode.code == createdPassCodeFlow.value?.code) {
            _passcodeStateFlow.update { PasscodeState.Confirmed(passCode) }
        } else {
            _passcodeStateFlow.update { PasscodeState.Error }
        }
    }

    init {
        viewModelScope.launch {
            passCodeUiFlow.collect { passCode ->
                _passcodeStateFlow.update { null }
                if (passCode.isFilled) {
                    when(setPasscodeStateFlow.value) {
                        SetPasscodeState.Create -> moveToConfirm(passCode)
                        SetPasscodeState.Confirm -> setPasscode(passCode)
                    }
                }
            }
        }
    }
}
