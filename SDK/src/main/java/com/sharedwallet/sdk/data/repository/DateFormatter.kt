package com.sharedwallet.sdk.data.repository

import java.text.SimpleDateFormat
import java.util.*

internal class DateFormatter {

    private val confirmTimeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val recentTimeFormatter = SimpleDateFormat("MM-dd HH:mm", Locale.ENGLISH)

    fun formatConfirmTime(date: Date): String = confirmTimeFormatter.format(date)

    fun formatRecentTime(date: Date): String = recentTimeFormatter.format(date)
}
