package com.sharedwallet.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationTransaction(
    @SerialName("coin_type") val coinType: Int,
    @SerialName("public_address") val publicAddress: String,
    @SerialName("transaction_hash") val transactionId: String,
)
