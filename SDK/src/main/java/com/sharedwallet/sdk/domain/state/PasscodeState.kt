package com.sharedwallet.sdk.domain.state

import com.sharedwallet.sdk.domain.models.PassCode

sealed class PasscodeState {
    class Confirmed(val passCode: PassCode): PasscodeState()
    object Error: PasscodeState()
}
