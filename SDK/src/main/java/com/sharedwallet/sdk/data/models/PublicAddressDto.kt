package com.sharedwallet.sdk.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PublicAddressDto(
    @SerialName("name") val name: String,
    @SerialName("coin_type") val coinType: Int,
    @SerialName("address") val address: String,
    @SerialName("contract_address") val contractAddress: String,
)
