package com.sharedwallet.sdk.nav.graph

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.utils.inCurrentStackStateFlow
import com.sharedwallet.sdk.domain.utils.removeInCurrentStack
import com.sharedwallet.sdk.domain.utils.setInPreviousStack
import com.sharedwallet.sdk.nav.Graphs
import com.sharedwallet.sdk.nav.Screens
import com.sharedwallet.sdk.nav.currencyArgument
import com.sharedwallet.sdk.screen.HomeScreen
import com.sharedwallet.sdk.screen.RecentRecordsScreen
import com.sharedwallet.sdk.screen.address.AddAddressScreen
import com.sharedwallet.sdk.screen.address.AddressListScreen
import com.sharedwallet.sdk.screen.currency.CurrencyDetailsScreen
import com.sharedwallet.sdk.screen.passcode.EnterPassCodeScreen
import com.sharedwallet.sdk.screen.scan.ScanQrCodeScreen
import com.sharedwallet.sdk.screen.splash.SplashScreen
import com.sharedwallet.sdk.screen.transaction.TransactionDetailsScreen

internal fun NavGraphBuilder.mainGraph(
    navController: NavController,
    onBackClick: () -> Unit,
) {

    composable(Screens.Splash) {
        SplashScreen(
            onBackClick = onBackClick,
            onCreateClick = { navController.navigate(Graphs.Register) },
            onRecoverClick = { navController.navigate(Graphs.Recovery) },
            userAlreadyRegistered = {
                navController.navigate(Screens.Home) {
                    popUpTo(Screens.Splash) { inclusive = true }
                }
            },
        )
    }
    registerGraph(navController)
    recoveryGraph(navController)
    composable(
        route = Screens.Home,
        deepLinks = listOf(navDeepLink { uriPattern = Screens.HomeDeepLink }),
    ) {
        HomeScreen(
            onBackClick = onBackClick,
            onRecordsClick = { navController.navigate(Screens.RecentRecords) },
        ) { navController.navigate(Screens.currencyDetailsPath(it)) }
    }
    composable(Screens.CurrencyDetails) { navBackStack ->
        val currency = remember { navBackStack.arguments?.currencyArgument!! }
        CurrencyDetailsScreen(
            onBackClick = { navController.popBackStack() },
            onRecordClick = {
                navController.navigate(Screens.transactionDetailsPath(currency, it.transactionHash))
            },
            onTransferClick = {
                navController.navigate(Graphs.transferPath(currency))
            },
            onReceiveClick = {
                navController.navigate(Graphs.receivePath(currency))
            },
        )
    }
    composable(Screens.RecentRecords) {
        RecentRecordsScreen(
            onBackClick = { navController.popBackStack() },
        ) {
            navController.navigate(Screens.transactionDetailsPath(it.currency, it.transactionHash))
        }
    }
    composable(
        route = Screens.TransactionDetails,
        deepLinks = listOf(navDeepLink { uriPattern = Screens.TransactionDetailsLink }),
    ) { backStackEntry ->
        val context = LocalContext.current
        val currency = remember { backStackEntry.arguments?.currencyArgument!! }
        TransactionDetailsScreen(
            onBackClick = {
                val popped = navController.popBackStack()
                if (!popped) {
                    (context as? Activity)?.finish()
                }
            },
            onSaveIdClick = {
                navController.navigate(
                    Screens.addAddressPath(
                        currency = currency,
                        addressId = it,
                    )
                )
            }
        )
    }
    transferGraph(navController)
    receiveGraph(navController)
    composable(Screens.AddressList) { backStackEntry ->
        val currency = remember { backStackEntry.arguments?.currencyArgument!! }
        AddressListScreen(
            currency = currency,
            onBackClick = { navController.popBackStack() },
            onAddressClick = {
                navController.setInPreviousStack(Keys.SELECTED_ADDRESS_ID, it.id)
                navController.popBackStack()
            },
            onAddClick = {
                navController.navigate(
                    Screens.addAddressPath(currency = currency)
                )
            }
        )
    }
    composable(
        route = Screens.AddAddress,
        arguments = Screens.AddAddressArguments,
    ) { backStackEntry ->
        val selectedAddressId by navController
            .inCurrentStackStateFlow<String>(Keys.SELECTED_ADDRESS_ID)
            .collectAsStateWithLifecycle()
        AddAddressScreen(
            currency = backStackEntry.arguments?.currencyArgument!!,
            selectedAddressId = selectedAddressId,
            onConsumeSelectedAddressRequest = { navController.removeInCurrentStack(Keys.SELECTED_ADDRESS_ID) },
            onBackClick = { navController.popBackStack() },
            onScanClick = { navController.navigate(Screens.ScanQrCode) },
            onSaved = { navController.popBackStack() },
        )
    }
    composable(Screens.ScanQrCode) {
        ScanQrCodeScreen(
            onBackClick = { navController.popBackStack() },
            onScanCode = {
                navController.setInPreviousStack(Keys.SELECTED_ADDRESS_ID, it)
                navController.popBackStack()
            },
        )
    }
    composable(Screens.ConfirmPasscode) {
        EnterPassCodeScreen(
            onBackClick = { navController.popBackStack() },
            onConfirmed = { passcode ->
                navController.setInPreviousStack(Keys.CONFIRMED_CODE, passcode)
                navController.popBackStack()
            },
        )
    }
}
