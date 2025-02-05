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
import androidx.navigation.navigation
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.utils.feeCurrency
import com.sharedwallet.sdk.domain.utils.inCurrentStackStateFlow
import com.sharedwallet.sdk.domain.utils.removeInCurrentStack
import com.sharedwallet.sdk.domain.utils.setInPreviousStack
import com.sharedwallet.sdk.nav.Graphs
import com.sharedwallet.sdk.nav.Screens
import com.sharedwallet.sdk.nav.currencyArgument
import com.sharedwallet.sdk.screen.transfer.GasFeeScreen
import com.sharedwallet.sdk.screen.transfer.TransferScreen

internal fun NavGraphBuilder.transferGraph(navController: NavController) {
    navigation(startDestination = Screens.Transfer, route = Graphs.Transfer) {
        composable(
            route = Screens.Transfer,
            deepLinks = listOf(navDeepLink { uriPattern = Graphs.TransferLink }),
        ) { navBackStack ->
            val confirmedPasscode by navController
                .inCurrentStackStateFlow<String>(Keys.CONFIRMED_CODE)
                .collectAsStateWithLifecycle()
            val context = LocalContext.current
            val selectedAddressId by navController
                .inCurrentStackStateFlow<String>(Keys.SELECTED_ADDRESS_ID)
                .collectAsStateWithLifecycle()
            val selectedGasFee by navController
                .inCurrentStackStateFlow<Double>(Keys.SELECTED_GAS_FEE)
                .collectAsStateWithLifecycle()
            val currency = remember { navBackStack.arguments?.currencyArgument!! }

            TransferScreen(
                selectedAddressId = selectedAddressId,
                selectedGasFee = selectedGasFee,
                confirmedPasscode = confirmedPasscode,
                onBackClick = {
                    val popped = navController.popBackStack()
                    if (!popped) {
                        (context as? Activity)?.finish()
                    }
                },
                onAddressListClick = { navController.navigate(Screens.addressListPath(currency)) },
                onScanClick = { navController.navigate(Screens.ScanQrCode) },
                onMinerFeeClick = { gasPrices, selectedFee ->
                    navController.navigate(Screens.gasFeePath(currency, gasPrices, selectedFee))
                },
                onPasscodeRequest = { navController.navigate(Screens.ConfirmPasscode) },
                onConsumePasscodeRequest = { navController.removeInCurrentStack(Keys.CONFIRMED_CODE) },
                onConsumeSelectedAddressRequest = { navController.removeInCurrentStack(Keys.SELECTED_ADDRESS_ID) },
                onConsumeSelectedGasRequest = { navController.removeInCurrentStack(Keys.SELECTED_GAS_FEE) },
                onTransferSent = {
                    navController.navigate(Screens.transactionDetailsPath(currency, it)) {
                        popUpTo(Screens.Transfer) { inclusive = true }
                    }
                },
            )
        }
        composable(Screens.GasFee) { navBackStack ->
            GasFeeScreen(
                feeCurrency = navBackStack.arguments?.currencyArgument!!.feeCurrency,
                onBackClick = { navController.popBackStack() },
                onSelectGasPrice = {
                    navController.setInPreviousStack(Keys.SELECTED_GAS_FEE, it)
                    navController.popBackStack()
                }
            )
        }
    }
}
