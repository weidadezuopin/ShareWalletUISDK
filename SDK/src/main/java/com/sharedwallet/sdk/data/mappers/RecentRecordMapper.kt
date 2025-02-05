package com.sharedwallet.sdk.data.mappers

import com.sharedwallet.sdk.data.models.RecentRecordDto
import com.sharedwallet.sdk.data.repository.CurrencyFormatter
import com.sharedwallet.sdk.data.repository.DateFormatter
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.RecordState
import com.sharedwallet.sdk.domain.models.Record
import com.sharedwallet.sdk.domain.utils.doesPayFees
import java.util.*

internal fun RecentRecordDto.toRecord(
    formatter: CurrencyFormatter,
    dateFormatter: DateFormatter,
): Record {
    val currency = coinType.currencyTypeFromName() ?: CurrencyType.BTC
    val isSent = transactionType == "transfer"
    return Record(
        id = id.toString(),
        transactionHash = transactionId,
        state = when (state) {
            "success" -> RecordState.Succeed
            else -> RecordState.Failed
        },
        isSent = isSent,
        currency = currency,
        amount = formatter.formatAmount(
            value = if (currency.doesPayFees && isSent) amount + networkFee else amount,
            currencyType = currency,
        ),
        otherAddress = oppositeAddress,
        date = dateFormatter.formatRecentTime(Date(confirmationTime * 1000)),
    )
}
