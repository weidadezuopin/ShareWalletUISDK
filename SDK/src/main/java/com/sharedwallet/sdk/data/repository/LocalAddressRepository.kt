package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import com.sharedwallet.sdk.data.mappers.toAddress
import com.sharedwallet.sdk.data.mappers.toAddressDto
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.models.Address
import com.sharedwallet.sdk.domain.state.RequestState
import com.sharedwallet.sdk.domain.utils.feeCurrency

internal class LocalAddressRepository(
    private val walletSdkDataSource: WalletSdkDataSource,
) {

    suspend fun getAddressList(currencyType: CurrencyType) =
        when(val request = walletSdkDataSource.getLocalUserAddressBook(currencyType.feeCurrency.keyType)) {
            is RequestState.Error -> RequestState.Error(request.e)
            is RequestState.Success -> RequestState.Success(request.data.map { it.toAddress() })
        }

    suspend fun addAddress(currencyType: CurrencyType, address: Address) =
        walletSdkDataSource.addAddressBook(
            currencyType = currencyType.feeCurrency.keyType,
            address = address.toAddressDto(),
        )

    suspend fun deleteAddress(currencyType: CurrencyType, address: Address) =
        (walletSdkDataSource.deleteAddressBook(
            currencyType = currencyType.feeCurrency.keyType,
            addressId = address.id,
        ) as? RequestState.Success)?.data ?: false
}
