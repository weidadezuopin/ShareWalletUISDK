package com.sharedwallet.sdk.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.TransactionType
import com.sharedwallet.sdk.domain.models.Record
import kotlinx.coroutines.flow.Flow

internal class RecordsRepository(
    private val walletSdkDataSource: WalletSdkDataSource,
    private val currencyFormatter: CurrencyFormatter,
) {

    fun getRecords(
        currencyType: CurrencyType,
        transactionType: TransactionType,
    ): Flow<PagingData<Record>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 15,
            ),
            pagingSourceFactory = {
                RecordPagingSource(
                    walletSdkDataSource = walletSdkDataSource,
                    formatter = currencyFormatter,
                    currencyType = currencyType,
                    transactionType = transactionType,
                )
            }
        ).flow
    }
}
