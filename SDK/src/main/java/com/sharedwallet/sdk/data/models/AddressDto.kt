package com.sharedwallet.sdk.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AddressDto(
    @SerialName("name") val name: String,
    @SerialName("address") val id: String,
)
