package com.sharedwallet.sdk.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CoinRatioDto(
    @SerialName("id") val coinType: Int,
    @SerialName("usd") val usd: Double,
    @SerialName("yuan") val yuan: Double,
    @SerialName("euro") val euro: Double,
)
