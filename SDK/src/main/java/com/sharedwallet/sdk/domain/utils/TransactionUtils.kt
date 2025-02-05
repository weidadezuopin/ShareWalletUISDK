package com.sharedwallet.sdk.domain.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.RecordState
import com.sharedwallet.sdk.domain.models.Transaction
import com.sharedwallet.sdk.theme.SwTheme

val Transaction.isReceived: Boolean
    get() = !isSent

val Transaction.stateIconRes: Int
    get() = when (state) {
        RecordState.Succeed -> R.drawable.sw_ic_succeed
        RecordState.Failed -> R.drawable.sw_ic_failed
        RecordState.Pending -> R.drawable.sw_ic_pending
    }

val RecordState.stringRes: Int
    get() = when (this) {
        RecordState.Succeed -> R.string.sw_success
        RecordState.Failed -> R.string.sw_fail
        RecordState.Pending -> R.string.sw_pending
    }

@Composable
fun RecordState.color(): Color = when (this) {
    RecordState.Succeed -> SwTheme.colors.succeed
    RecordState.Failed -> SwTheme.colors.failed
    RecordState.Pending -> SwTheme.colors.pending
}

val Transaction.isGasFeesAvailable: Boolean
    get() = isSent && state != RecordState.Pending && currency.feeCurrency == CurrencyType.ETH

val Transaction.canShowConfirmations: Boolean
    get() = isSent && !isGasFeesAvailable
