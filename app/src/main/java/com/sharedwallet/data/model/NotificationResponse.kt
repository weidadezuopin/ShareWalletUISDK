package com.sharedwallet.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponse(
    @SerialName("clientMsgID") val clientMessageId: String,
    @SerialName("msgContent") val messageContent: String,
)
