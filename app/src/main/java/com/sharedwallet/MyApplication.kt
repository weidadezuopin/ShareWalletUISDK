package com.sharedwallet

import android.app.Application
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.sharedwallet.sdk.WalletAPI

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        WalletAPI.initWallet(this)
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        Log.i("JPush-id", JPushInterface.getRegistrationID(this))
    }
}
