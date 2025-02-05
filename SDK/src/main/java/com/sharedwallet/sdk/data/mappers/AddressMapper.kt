package com.sharedwallet.sdk.data.mappers

import com.sharedwallet.sdk.data.models.AddressDto
import com.sharedwallet.sdk.domain.models.Address

internal fun AddressDto.toAddress() = Address(
    id = id,
    name = name,
)

internal fun Address.toAddressDto() = AddressDto(
    id = id,
    name = name,
)
