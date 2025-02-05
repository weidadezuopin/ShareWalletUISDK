package com.sharedwallet.sdk.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sharedwallet.sdk.data.datasource.WalletSdkDataSource
import com.sharedwallet.sdk.data.mappers.toRecord
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.TransactionType
import com.sharedwallet.sdk.domain.models.Record
import com.sharedwallet.sdk.domain.state.RequestState

internal class RecordPagingSource(
    private val walletSdkDataSource: WalletSdkDataSource,
    private val formatter: CurrencyFormatter,
    private val currencyType: CurrencyType,
    private val transactionType: TransactionType,
) : PagingSource<Int, Record>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Record> {
        val nextPageNumber = params.key ?: 1
        val request = when (val request = walletSdkDataSource.getPublicAddress(currencyType.keyType)) {
            is RequestState.Error -> request
            is RequestState.Success -> {
                if (request.data.isNotEmpty()) {
                    walletSdkDataSource.getTransactionList(
                        currencyType = currencyType.keyType,
                        publicAddress = request.data.first().address,
                        transactionType = transactionType.key,
                        pageNumber = nextPageNumber,
                        loadSize = params.loadSize,
                    )
                } else {
                    RequestState.Error(IllegalArgumentException(""))
                }
            }

        }

        return when(request) {
            is RequestState.Error -> LoadResult.Error(request.e)
            is RequestState.Success -> {
                val newList = request.data.list.map {
                    it.toRecord(
                        currency = currencyType,
                        formatter = formatter,
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
