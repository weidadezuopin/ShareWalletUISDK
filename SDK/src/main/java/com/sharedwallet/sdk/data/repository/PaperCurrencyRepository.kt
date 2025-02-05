package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.data.datasource.DataStoreDataSource
import com.sharedwallet.sdk.domain.enums.PaperCurrency
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class PaperCurrencyRepository(
    private val dataStoreDataSource: DataStoreDataSource,
) {

    val selected = dataStoreDataSource.preferencesFlow
        .map { preferences ->
            preferences[DataStoreDataSource.KEY_PAPER_CURRENCY]?.get(0) ?: PaperCurrency.YUAN.symbol
        }
        .map { symbol -> PaperCurrency.values().first { it.symbol == symbol } }

    suspend fun getCurrency() = selected.first()

    suspend fun setCurrency(paperCurrency: PaperCurrency) {
        dataStoreDataSource.edit(DataStoreDataSource.KEY_PAPER_CURRENCY, paperCurrency.symbol.toString())
    }
}
