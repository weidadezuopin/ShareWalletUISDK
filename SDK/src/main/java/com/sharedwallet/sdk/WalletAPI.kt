package com.sharedwallet.sdk

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.sharedwallet.sdk.di.WalletKoinContext
import com.sharedwallet.sdk.di.swKoin
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.enums.PaperCurrency
import com.sharedwallet.sdk.domain.usecase.*
import com.sharedwallet.sdk.domain.utils.TransactionId
import com.sharedwallet.sdk.nav.Graphs
import com.sharedwallet.sdk.nav.Screens
import kotlinx.coroutines.runBlocking

@Suppress("unused", "MemberVisibilityCanBePrivate")
object WalletAPI {

    /**
     * Initiate dependency injection component for shared wallet.
     */
    fun initWallet(applicationContext: Context) {
        WalletKoinContext.koinApp = swKoin(applicationContext)
    }

    /**
     * Initiate the wallet database. It must be called before wallet is opened
     * or before calling any API.
     *
     * @param clientId Unique ID to a client, it will be used to identify
     * all wallets that belong to this user.
     */
    suspend fun initWalletSdk(
        clientId: String,
        apiLink: String,
    ) {
        InitSdkUseCase(clientId, apiLink).invoke()
    }

    /**
     * Initiate dependency injection component for shared wallet.
     */
    suspend fun cachePaperCurrency(paperCurrency: PaperCurrency) {
        UpdatePaperCurrencyUseCase(paperCurrency).invoke()
    }

    /**
     * Initiate dependency injection component for shared wallet.
     */
    suspend fun cacheLanguage(language: String?) {
        UpdateLanguageUseCase(language).invoke()
    }

    /**
     * Check if user already registered.
     */
    suspend fun isUserRegistered() =
        UserRegisteredUseCase().invoke()

    /**
     * Check if a friend already registered to shared wallet.
     */
    suspend fun isOtherUserRegistered(otherUserId: String, currencyType: CurrencyType) =
        OtherUserRegisteredUseCase(otherUserId, currencyType).invoke()

    /**
     * Get available coins from the server, or from cached data if any error happened.
     */
    suspend fun getAvailableCurrencies() =
        AvailableCurrenciesUseCase().invoke()

    /**
     * Update the transaction details screen for the [transactionId] provided.
     * It must be called when a transaction is updated. For example, when
     * a notification is received.
     */
    fun updateTransactionDetails(transactionId: String) {
        UpdateTransactionUseCase(transactionId).invoke()
    }

    /**
     * Launch shared wallet activity.
     */
    fun openSharedWallet(context: Context) {
        context.startActivity(
            Intent(context, SharedWalletActivity::class.java)
        )
    }

    private fun openDeepLink(context: Context, uri: Uri) {
        val isLoggedIn = runBlocking { isUserRegistered() }
        if (isLoggedIn) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    uri,
                    context,
                    SharedWalletActivity::class.java,
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
            )
        } else {
            openSharedWallet(context)
        }
    }

    /**
     * Open home screen immediately if user already has a wallet,
     * or splash screen in other ways.
     */
    fun openHome(
        context: Context,
    ) {
        openDeepLink(
            context = context,
            uri = Screens.HomeDeepLink.toUri(),
        )
    }

    /**
     * Open transaction details screen immediately if user already has a wallet,
     * or splash screen in other ways.
     */
    fun openTransaction(
        context: Context,
        currencyType: CurrencyType,
        transactionId: TransactionId,
    ) {
        openDeepLink(
            context = context,
            uri = Screens.transactionDetailsDeepLink(currencyType, transactionId),
        )
    }

    /**
     * Open transfer screen immediately if user already has a wallet,
     * or splash screen in other ways.
     */
    fun openTransfer(
        context: Context,
        currencyType: CurrencyType,
        otherUserId: String,
    ) {
        openDeepLink(
            context = context,
            uri = Graphs.transferDeepLink(currencyType, otherUserId),
        )
    }

    /**
     * Flow of events sent each time user make a transaction.
     */
    val transferEventsFlow
        get() = TransferEventUseCase().invoke()
}
