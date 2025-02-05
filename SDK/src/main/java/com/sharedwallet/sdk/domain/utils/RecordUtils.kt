package com.sharedwallet.sdk.domain.utils

import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.models.Record

val Record.isReceived: Boolean
    get() = !isSent

val Record.plusOrMinus: Char
    get() = if (isSent) '-' else '+'

val Record.recentSendStateDrawableRes: Int
    get() = if(isSent) R.drawable.sw_ic_recent_record_sent else R.drawable.sw_ic_recent_record_received

val Record.sendStateDrawableRes: Int
    get() = if(isSent) R.drawable.sw_ic_record_sent else R.drawable.sw_ic_record_received
