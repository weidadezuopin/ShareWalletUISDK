package com.sharedwallet.sdk.nav.graph

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.utils.inCurrentStackStateFlow
import com.sharedwallet.sdk.domain.utils.removeInCurrentStack
import com.sharedwallet.sdk.nav.Graphs
import com.sharedwallet.sdk.nav.Screens
import com.sharedwallet.sdk.nav.currencyArgument
import com.sharedwallet.sdk.screen.receive.PrivateKeyScreen
import com.sharedwallet.sdk.screen.receive.PrivateKeyWarningScreen
import com.sharedwallet.sdk.screen.receive.ReceiveQrCodeScreen
import com.sharedwallet.sdk.screen.receive.ShowSeedPhraseScreen

internal fun NavGraphBuilder.receiveGraph(navController: NavController) {
    navigation(startDestination = Screens.ReceiveQrCode, route = Graphs.Receive) {
        composable(Screens.ReceiveQrCode) { navBackStack ->
            val confirmedPasscode by navController
                .inCurrentStackStateFlow<String>(Keys.CONFIRMED_CODE)
                .collectAsStateWithLifecycle()
            val currency = remember { navBackStack.arguments?.currencyArgument!! }

            ReceiveQrCodeScreen(
                currency = currency,
                confirmedPasscode = confirmedPasscode,
                onBackClick = { navController.popBackStack() },
                onPasscodeRequest = { navController.navigate(Screens.ConfirmPasscode) },
                onConsumePasscodeRequest = { navController.removeInCurrentStack(Keys.CONFIRMED_CODE) },
                onPrivateKeyRequest = { passcode ->
                    navController.navigate(Screens.privateKeyWarningPath(passcode, currency))
                },
                onShowSeedPhraseRequest = { navController.navigate(Screens.showSeedPhrasePath(it)) },
            )
        }
        composable(Screens.PrivateKeyWarning) { navBackStack ->
            val passcode = remember { navBackStack.arguments?.getString(Keys.CONFIRMED_CODE)!! }
            val currency = remember { navBackStack.arguments?.currencyArgument!! }

            PrivateKeyWarningScreen(
                onNextClick = {
                    navController.navigate(Screens.privateKeyPath(passcode, currency)) {
                        popUpTo(navController.previousBackStackEntry?.destination?.route!!)
                    }
                },
            )
        }
        composable(Screens.PrivateKey) {
            PrivateKeyScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(Screens.ShowSeedPhrase) {
            ShowSeedPhraseScreen { navController.popBackStack() }
        }
    }
}
