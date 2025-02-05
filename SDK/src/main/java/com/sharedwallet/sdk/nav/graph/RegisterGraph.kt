package com.sharedwallet.sdk.nav.graph

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.nav.Graphs
import com.sharedwallet.sdk.nav.Screens
import com.sharedwallet.sdk.screen.passcode.SetPasscodeScreen
import com.sharedwallet.sdk.screen.register.PhraseBackupScreen
import com.sharedwallet.sdk.screen.register.PhraseConfirmScreen
import com.sharedwallet.sdk.screen.register.PhraseInfoScreen
import com.sharedwallet.sdk.screen.register.PhraseWarningScreen

internal fun NavGraphBuilder.registerGraph(navController: NavController) {
    navigation(startDestination = Screens.PhraseInfo, route = Graphs.Register) {
        composable(Screens.PhraseInfo) {
            PhraseInfoScreen(
                onBackClick = { navController.popBackStack() },
                onNextClick = { navController.navigate(Screens.SetPasscodeRegister) },
            )
        }
        composable(Screens.SetPasscodeRegister) {
            SetPasscodeScreen(
                onBackClick = { navController.popBackStack() },
                onPasscodeConfirmed = { navController.navigate(Screens.phraseWarningPath(it)) },
            )
        }
        composable(Screens.PhraseWarning) { navBackStack ->
            val passcode = remember { navBackStack.arguments?.getString(Keys.CONFIRMED_CODE)!! }
            PhraseWarningScreen(
                onNextClick = { navController.navigate(Screens.phraseBackupPath(passcode)) },
            )
        }
        composable(Screens.PhraseBackup) {
            PhraseBackupScreen(
                onNotNowClick = {
                    navController.navigate(Screens.Home) {
                        popUpTo(Screens.Splash) { inclusive = true }
                    }
                },
                onConfirmClick = { seedPhrase ->
                    navController.navigate(
                        Screens.phraseConfirmPath(
                            seedPhrase.joinToString(separator = ",") { it.word }
                        )
                    )
                },
            )
        }
        composable(Screens.PhraseConfirm) {
            PhraseConfirmScreen(
                onBackClick = { navController.popBackStack() },
                onConfirmed = {
                    navController.navigate(Screens.Home) {
                        popUpTo(Screens.Splash) { inclusive = true }
                    }
                },
            )
        }
    }
}
