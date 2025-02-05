package com.sharedwallet.sdk.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RecordDto(
    @SerialName("transaction_hash") val transactionHash: String,
    @SerialName("uuID") val uuid: String,
    @SerialName("sender_address") val fromAddress: String,
    @SerialName("receiver_address") val toAddress: String,
    @SerialName("amount_conv") val amount: Double,
    @SerialName("fee_conv") val fee: Double,
    @SerialName("is_send") val isSent: Boolean,
    @SerialName("status") val status: Int,
)
