package com.sharedwallet.sdk.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RecentRecordDto(
    @SerialName("id") val id: Int,
    @SerialName("txid") val transactionId: String,
    @SerialName("transaction_type") val transactionType: String,
    @SerialName("user_address") val userAddress: String,
    @SerialName("opposite_address") val oppositeAddress: String,
    @SerialName("coin_type") val coinType: String,
    @SerialName("state") val state: String,
    @SerialName("amount_of_coins") val amount: Double,
    @SerialName("network_fee") val networkFee: Double,
    @SerialName("confirmation_time") val confirmationTime: Long,
)
