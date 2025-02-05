package com.sharedwallet.sdk.data.repository

import com.sharedwallet.sdk.data.datasource.DataStoreDataSource
import kotlinx.coroutines.flow.map

internal class TutorialRepository(
    private val dataStoreDataSource: DataStoreDataSource,
) {

    val tutorialFlow = dataStoreDataSource.preferencesFlow
        .map { preferences ->
            preferences[DataStoreDataSource.KEY_SHOW_TUTORIAL] ?: true
        }

    suspend fun setShown() {
        dataStoreDataSource.edit(DataStoreDataSource.KEY_SHOW_TUTORIAL, false)
    }
}
