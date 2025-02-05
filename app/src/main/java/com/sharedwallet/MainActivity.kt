package com.sharedwallet

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import cn.jpush.android.api.JPushInterface
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.sharedwallet.data.model.NotificationResponse
import com.sharedwallet.data.model.NotificationTransaction
import com.sharedwallet.sdk.WalletAPI
import com.sharedwallet.sdk.data.mappers.currencyTypeFromId
import com.sharedwallet.sdk.domain.enums.PaperCurrency
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class MainActivity : AppCompatActivity() {

    private val paperCoins = listOf(PaperCurrency.YUAN, PaperCurrency.USD, PaperCurrency.EURO)

//    private val baseUrl = "http://devwalletapi.ddns.net:81"
    private val baseUrl = "https://api.sharewallet-test.com"

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RequestNotificationPermission()
            MaterialTheme {
                val scope = rememberCoroutineScope()
                var clientId: String by rememberSaveable { mutableStateOf(getCachedClientId()) }
                var selectedCurrency by rememberSaveable { mutableStateOf(paperCoins.first()) }
                var loading by rememberSaveable { mutableStateOf(false) }

                LaunchedEffect(key1 = selectedCurrency) {
                    WalletAPI.cachePaperCurrency(selectedCurrency)
                }

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row {
                            paperCoins.forEachIndexed { index, currency ->
                                val checked = currency == selectedCurrency
                                val tint by animateColorAsState(if (checked) Color(0xFFCD4AE4) else Color(0xFFB0BEC5))
                                OutlinedButton(
                                    onClick = { selectedCurrency = currency },
                                    border = BorderStroke(1.dp, tint),
                                    shape = when (index) {
                                        0 -> RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                                        paperCoins.lastIndex -> RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                                        else -> RectangleShape
                                    },
                                ) {
                                    Text(text = "${currency.symbol}", color = tint)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = clientId,
                            onValueChange = { text ->
                                if (text.length <= 30 && !text.trim().any { it.isWhitespace() }) {
                                    clientId = text
                                }
                            },
                            label = { Text("Enter client ID") },
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedContent(targetState = loading) { isLoading ->
                            if (isLoading) {
                                CircularProgressIndicator()
                            } else {
                                Button(
                                    onClick = {
                                        scope.launch {
                                            loading = true
                                            WalletAPI.initWalletSdk(
                                                clientId = clientId,
                                                apiLink = baseUrl,
                                            )
                                            WalletAPI.openSharedWallet(
                                                context = this@MainActivity,
                                            )
                                            cacheClientId(clientId)
                                            JPushInterface.setAlias(this@MainActivity, 0, clientId)
                                            loading = false
                                        }
                                    },
                                ) {
                                    Text(text = "Open shared wallet sdk")
                                }
                            }
                        }
                        Text(text = "Environment: $baseUrl", style = MaterialTheme.typography.caption)
                    }
                }
            }
        }

        if(intent.getStringExtra("JMessageExtra") != null) {
            lifecycleScope.launch {
                WalletAPI.initWalletSdk(
                    clientId = getCachedClientId(),
                    apiLink = baseUrl,
                )
                try {
                    // TODO Avoid JSON decoding in this way in real app
                    val json: JsonObject =
                        Json.decodeFromString(intent.getStringExtra("JMessageExtra")!!)
                    val notificationResponse = Json.decodeFromString(
                        NotificationResponse.serializer(),
                        json["n_extras"]?.toString()!!,
                    )
                    val notificationTransaction = Json.decodeFromString(
                        NotificationTransaction.serializer(),
                        notificationResponse.messageContent,
                    )
                    WalletAPI.openTransaction(
                        context = this@MainActivity,
                        currencyType = notificationTransaction.coinType.currencyTypeFromId()!!,
                        transactionId = notificationTransaction.transactionId,
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestNotificationPermission() {
        val lifecycleOwner = LocalLifecycleOwner.current
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionState =
                rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
            DisposableEffect(key1 = lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START) {
                        permissionState.launchPermissionRequest()
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
            }
        }
    }

    private fun getCachedClientId() = runBlocking {
        dataStore.data
            .map { preferences ->
                preferences[KEY_CLIENT_ID] ?: ""
            }
            .first()
    }

    private fun cacheClientId(clientId: String) {
        lifecycleScope.launch {
            dataStore.edit { settings ->
                settings[KEY_CLIENT_ID] = clientId
            }
        }
    }
}
