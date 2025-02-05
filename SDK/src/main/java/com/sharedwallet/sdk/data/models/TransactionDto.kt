package com.sharedwallet.sdk.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TransactionDto(
    @SerialName("transaction_hash") val hash: String,
    @SerialName("status") val status: Int,
    @SerialName("is_send") val isSent: Boolean,
    @SerialName("sender_address") val fromAddress: String,
    @SerialName("sender_account") val sender: AddressDto? = null,
    @SerialName("receiver_address") val toAddress: String,
    @SerialName("receiver_account") val receiver: AddressDto? = null,
    @SerialName("amount_conv") val amount: Double,
    @SerialName("fee_conv") val fees: Double?,
    @SerialName("gas_price_conv") val gasPrice: Double?,
    @SerialName("gas_used") val gasUsed: Long?,
    @SerialName("gas_limit") val gasLimit: Long?,
    @SerialName("confirm_block_number") val blockNumber: String?,
    @SerialName("confirm_time") val confirmTimeUnix: ULong?,
)
