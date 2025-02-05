package com.sharedwallet.sdk.data.mappers

import com.sharedwallet.sdk.data.models.TransactionDto
import com.sharedwallet.sdk.data.repository.CurrencyFormatter
import com.sharedwallet.sdk.data.repository.DateFormatter
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.RecordState
import com.sharedwallet.sdk.domain.models.Transaction
import java.util.*

internal fun TransactionDto.toTransaction(
    currencyType: CurrencyType,
    formatter: CurrencyFormatter,
    dateFormatter: DateFormatter,
) = Transaction(
    currency = currencyType,
    toId = toAddress,
    fromId = fromAddress,
    otherName = if (isSent) receiver?.name else sender?.name,
    transactionId = hash,
    state = when(status) {
        RecordState.Succeed.key -> RecordState.Succeed
        RecordState.Failed.key -> RecordState.Failed
        else -> RecordState.Pending
    },
    isSent = isSent,
    amount = formatter.formatAmount(amount, currencyType),
    networkFee = fees?.let { formatter.formatGas(it) },
    gasPrice = gasPrice?.let { formatter.formatGas(it) },
    gasUsed = gasUsed?.toString(),
    gasLimit = gasLimit?.toString(),
    confirmDate = confirmTimeUnix?.let { dateFormatter.formatConfirmTime(Date(it.toLong() * 1000)) },
    confirmations = blockNumber,
)
