package com.sharedwallet.sdk.data.mappers

import com.sharedwallet.sdk.data.models.RecordDto
import com.sharedwallet.sdk.data.repository.CurrencyFormatter
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.RecordState
import com.sharedwallet.sdk.domain.models.Record
import com.sharedwallet.sdk.domain.utils.doesPayFees

internal fun RecordDto.toRecord(
    currency: CurrencyType,
    formatter: CurrencyFormatter,
) = Record(
    id = uuid,
    transactionHash = transactionHash,
    state = when(status) {
        RecordState.Succeed.key -> RecordState.Succeed
        RecordState.Pending.key -> RecordState.Pending
        else -> RecordState.Failed
    },
    isSent = isSent,
    currency = currency,
    amount = formatter.formatAmount(
        value = if (currency.doesPayFees && isSent) amount + fee else amount,
        currencyType = currency,
    ),
    otherAddress = if (isSent) toAddress else fromAddress,
    date = "",
)
