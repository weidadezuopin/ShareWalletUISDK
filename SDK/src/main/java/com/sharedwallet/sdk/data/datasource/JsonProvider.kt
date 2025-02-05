package com.sharedwallet.sdk.data.datasource

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json

internal class JsonProvider(
    private val json: Json,
) {

    fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String =
        json.encodeToString(serializer, value)

    fun <T> decodeFromString(serializer: DeserializationStrategy<T>, value: String): T =
        json.decodeFromString(serializer, value)
}
