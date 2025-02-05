package com.sharedwallet.sdk.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RecentRecordListResponse(
    @SerialName("funds_log") val list: List<RecentRecordDto>,
    @SerialName("page") val page: Int,
    @SerialName("page_size") val pageSize: Int,
)
