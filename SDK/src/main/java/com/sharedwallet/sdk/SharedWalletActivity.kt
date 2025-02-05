package com.sharedwallet.sdk

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.Consumer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.sharedwallet.sdk.data.repository.LanguageRepository
import com.sharedwallet.sdk.di.WalletKoinComponent
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.nav.Screens
import com.sharedwallet.sdk.nav.graph.mainGraph
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.viewmodel.MainViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.createActivityScope
import org.koin.core.component.inject
import org.koin.core.scope.Scope
import java.util.*


class SharedWalletActivity : AppCompatActivity(), WalletKoinComponent, AndroidScopeComponent {

    override val scope: Scope by lazy { createActivityScope() }

    private val languageRepository: LanguageRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        setLanguage()
        super.onCreate(savedInstanceState)
        setLanguage()

        setContent {
            SharedWalletTheme {
                val context = LocalContext.current
                val viewModel: MainViewModel = koinSwViewModel()
                val navController = rememberNavController()

                LaunchedEffect(key1 = Unit) {
                    viewModel.toastFlow.collect { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }

                DisposableEffect(Unit) {
                    val listener = Consumer<Intent> { intent ->
                        val uri = intent.data
                        if (uri != null && viewModel.isUserLoggedIn()) {
                            navController.navigate(
                                uri,
                                navOptions {
                                    popUpTo(Screens.TransactionDetails) { inclusive = true }
                                },
                            )
                        }
                    }
                    addOnNewIntentListener(listener)
                    onDispose { removeOnNewIntentListener(listener) }
                }

                Scaffold { padding ->
                    NavHost(
                        modifier = Modifier.padding(padding),
                        navController = navController,
                        startDestination = Screens.Splash,
                    ) {
                        mainGraph(
                            navController = navController,
                            onBackClick = { finish() },
                        )
                    }
                }
            }
        }
    }

    private fun setLanguage() {
        val lang = runBlocking { languageRepository.languageFlow.first() }
        if (lang != null) {
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }
}
