package com.sharedwallet.sdk.nav.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sharedwallet.sdk.nav.Graphs
import com.sharedwallet.sdk.nav.Screens
import com.sharedwallet.sdk.screen.passcode.SetPasscodeScreen
import com.sharedwallet.sdk.screen.recovery.EnterSeedPhraseScreen

internal fun NavGraphBuilder.recoveryGraph(navController: NavController) {
    navigation(startDestination = Screens.SetPasscodeRecover, route = Graphs.Recovery) {
        composable(Screens.SetPasscodeRecover) {
            SetPasscodeScreen(
                onBackClick = { navController.popBackStack() },
                onPasscodeConfirmed = {
                    navController.navigate(Screens.recoveryEnterPath(it)) {
                        popUpTo(navController.previousBackStackEntry?.destination?.route!!)
                    }
                },
            )
        }
        composable(Screens.RecoveryEnter) {
            EnterSeedPhraseScreen(
                onBackClick = { navController.popBackStack() },
                onRecovery = {
                    navController.navigate(Screens.Home) {
                        popUpTo(Screens.Splash) { inclusive = true }
                    }
                },
            )
        }
    }
}
