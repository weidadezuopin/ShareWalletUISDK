package com.sharedwallet.sdk.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import com.sharedwallet.sdk.data.mappers.toRecord
import com.sharedwallet.sdk.domain.models.Record
import com.sharedwallet.sdk.domain.state.RequestState
import kotlinx.coroutines.flow.Flow

internal class RecentRecordsRepository(
    private val walletSdkDataSource: WalletSdkDataSource,
    private val currencyFormatter: CurrencyFormatter,
    private val dateFormatter: DateFormatter,
) {

    fun getRecords(): Flow<PagingData<Record>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 15,
            ),
            pagingSourceFactory = {
                RecentRecordPagingSource(
                    walletSdkDataSource = walletSdkDataSource,
                    formatter = currencyFormatter,
                    dateFormatter = dateFormatter,
                )
            },
        ).flow
    }

    suspend fun getLastRecord(): RequestState<Record?> {
        val request = walletSdkDataSource.getRecentTransactions(
            pageNumber = 1,
            loadSize = 1,
        )
        return when (request) {
            is RequestState.Success -> {
                RequestState.Success(
                    request.data.list.firstOrNull()?.toRecord(
                        formatter = currencyFormatter,
                        dateFormatter = dateFormatter,
                    )
                )
            }
            is RequestState.Error -> request
        }
    }
}
