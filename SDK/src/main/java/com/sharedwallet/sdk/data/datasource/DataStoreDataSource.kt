package com.sharedwallet.sdk.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

internal class DataStoreDataSource(
    private val dataStore: DataStore<Preferences>,
) {

    val preferencesFlow = dataStore.data

    suspend fun <T> edit(key: Preferences.Key<T>, value: T?) {
        dataStore.edit { settings ->
            if (value == null) {
                settings.remove(key)
            } else {
                settings[key] = value
            }
        }
    }

    companion object {

        val KEY_SHOW_TUTORIAL = booleanPreferencesKey("key_show_tutorial")
        val KEY_LANGUAGE = stringPreferencesKey("key_language")
        val KEY_PAPER_CURRENCY = stringPreferencesKey("key_paper_currency")
    }
}
