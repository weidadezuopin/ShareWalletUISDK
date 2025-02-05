package com.sharedwallet.sdk.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RecordListResponse(
    @SerialName("transaction") val list: List<RecordDto>,
    @SerialName("page") val page: Int,
    @SerialName("page_size") val pageSize: Int,
)
