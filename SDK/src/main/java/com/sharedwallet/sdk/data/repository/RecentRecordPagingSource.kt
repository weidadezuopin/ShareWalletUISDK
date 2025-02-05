package com.sharedwallet.sdk.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import com.sharedwallet.sdk.data.mappers.toRecord
import com.sharedwallet.sdk.domain.models.Record
import com.sharedwallet.sdk.domain.state.RequestState

internal class RecentRecordPagingSource(
    private val walletSdkDataSource: WalletSdkDataSource,
    private val formatter: CurrencyFormatter,
    private val dateFormatter: DateFormatter,
) : PagingSource<Int, Record>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Record> {
        val nextPageNumber = params.key ?: 1
        val request = walletSdkDataSource.getRecentTransactions(
            pageNumber = nextPageNumber,
            loadSize = params.loadSize,
        )

        return when(request) {
            is RequestState.Error -> LoadResult.Error(request.e)
            is RequestState.Success -> {
                val newList = request.data.list.map {
                    it.toRecord(
                        formatter = formatter,
                        dateFormatter = dateFormatter,
                    )
                }
                LoadResult.Page(
                    data = newList,
                    prevKey = null,
                    nextKey = if(newList.size < params.loadSize) {
                        null
                    } else {
                        nextPageNumber + 1
                    },
                )
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Record>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
