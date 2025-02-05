package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.data.datasource.DataStoreDataSource
import kotlinx.coroutines.flow.map

internal class LanguageRepository(
    private val dataStoreDataSource: DataStoreDataSource,
) {

    val languageFlow = dataStoreDataSource.preferencesFlow
        .map { preferences ->
            preferences[DataStoreDataSource.KEY_LANGUAGE]
        }

    suspend fun setLanguage(language: String?) {
        dataStoreDataSource.edit(DataStoreDataSource.KEY_LANGUAGE, language)
    }
}
