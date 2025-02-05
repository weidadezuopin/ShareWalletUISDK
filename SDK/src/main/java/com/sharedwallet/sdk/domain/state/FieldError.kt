package com.sharedwallet.sdk.domain.state

sealed class FieldError {
    object Empty: FieldError()
    class Invalid(val reason: Int? = null): FieldError()

    companion object {
        const val REASON_BALANCE_NOT_ENOUGH = 1
    }
}
