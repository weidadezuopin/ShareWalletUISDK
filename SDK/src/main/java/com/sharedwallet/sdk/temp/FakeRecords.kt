package com.sharedwallet.sdk.temp

import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.RecordState
import com.sharedwallet.sdk.domain.models.Record

val fakeRecords = listOf(
    Record(
        id = "1",
        transactionHash = "1sa5d64asd231",
        state = RecordState.Succeed,
        isSent = true,
        currency = CurrencyType.ETH,
        amount = "0.01000000",
        otherAddress = "35468768435468",
        date = "10-14 03:45",
    ),
    Record(
        id = "2",
        transactionHash = "2sa5d64asd231",
        state = RecordState.Pending,
        isSent = true,
        currency = CurrencyType.TRX,
        amount = "0.01000000",
        otherAddress = "35468768435468",
        date = "10-14 03:45",
    ),
    Record(
        id = "3",
        transactionHash = "3sa5d64asd231",
        state = RecordState.Failed,
        isSent = true,
        currency = CurrencyType.BTC,
        amount = "0.01000000",
        otherAddress = "35468768435468",
        date = "10-14 03:45",
    ),
    Record(
        id = "4",
        transactionHash = "4sa5d64asd231",
        state = RecordState.Succeed,
        isSent = false,
        currency = CurrencyType.USDT_TRC20,
        amount = "0.01000000",
        otherAddress = "35468768435468",
        date = "10-14 03:45",
    ),
)
