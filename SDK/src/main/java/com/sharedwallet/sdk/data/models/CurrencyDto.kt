package com.sharedwallet.sdk.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CurrencyDto(
    @SerialName("coin_type") val coinType: Int,
    @SerialName("name") val shortName: String,
    @SerialName("description") val fullName: String,
)
