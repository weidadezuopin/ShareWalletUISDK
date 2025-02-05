package com.sharedwallet.sdk.temp

import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.RecordState
import com.sharedwallet.sdk.domain.models.Transaction

val fakeSucceedTransaction = Transaction(
    transactionId = "b3c9 5aca 193b 5294 38ef d493 df04 3e1c 6f6c 6427 7070 5a19 aa83 285d 0c2c a9ff",
    toId = "AKZKaV4sN5cFKwwGPtEmfUphfGzEML1X19FKV564FS5321SF564SFD561SF",
    fromId = "BKZKaV4sN5cFKwwGPtEmfUphfGzEML1X19S8W7H521N5JMET45S456FS8SD",
    currency = CurrencyType.ETH,
    state = RecordState.Succeed,
    isSent = true,
    amount = "1.00000036",
    confirmDate = "2022-10-26 18:35:30",
    networkFee = "0.014484841",
    gasPrice = "0.000262216007193823",
    gasLimit = "33,760",
    gasUsed = "22,507",
    confirmations = "85867",
)
