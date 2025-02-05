package com.sharedwallet

import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.sharedwallet.data.model.NotificationResponse
import com.sharedwallet.data.model.NotificationTransaction
import com.sharedwallet.sdk.WalletAPI
import kotlinx.serialization.json.Json

class PushReceiver : JPushMessageReceiver() {

    override fun onNotifyMessageArrived(
        context: Context,
        notificationMessage: NotificationMessage
    ) {
        super.onNotifyMessageArrived(context, notificationMessage)
        try {
            // TODO Avoid JSON decoding in this way in real app
            val notificationResponse = Json.decodeFromString(
                NotificationResponse.serializer(),
                notificationMessage.notificationExtras,
            )
            val notificationTransaction = Json.decodeFromString(
                NotificationTransaction.serializer(),
                notificationResponse.messageContent,
            )
            WalletAPI.updateTransactionDetails(notificationTransaction.transactionId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNotifyMessageOpened(context: Context, notificationMessage: NotificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage)
        val mIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        Log.i(TAG, "${notificationMessage.notificationTitle}: $${notificationMessage.notificationContent}")
        context.startActivity(mIntent)
    }

    companion object {
        private const val TAG = "PushReceiver"
    }
}