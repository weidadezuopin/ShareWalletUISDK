package com.sharedwallet.sdk.nav

import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.navArgument
import com.sharedwallet.sdk.domain.constants.Keys
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.utils.fromShortName

private const val rootUri = "wallet://shared"

internal object Screens {
    const val Splash = "splashScreen"

    const val PhraseInfo = "phraseScreen"
    const val SetPasscodeRegister = "setPasscodeRegisterScreen"
    const val PhraseWarning = "phraseWarningScreen?${Keys.CONFIRMED_CODE}={${Keys.CONFIRMED_CODE}}"
    fun phraseWarningPath(code: String) = "phraseWarningScreen?${Keys.CONFIRMED_CODE}=$code"
    const val PhraseBackup = "phraseBackupScreen?${Keys.CONFIRMED_CODE}={${Keys.CONFIRMED_CODE}}"
    fun phraseBackupPath(code: String) = "phraseBackupScreen?${Keys.CONFIRMED_CODE}=$code"
    const val PhraseConfirm = "phraseConfirmScreen?${Keys.SEED_PHRASE}={${Keys.SEED_PHRASE}}"
    fun phraseConfirmPath(seedPhrase: String) = "phraseConfirmScreen?${Keys.SEED_PHRASE}=$seedPhrase"

    const val SetPasscodeRecover = "setPasscodeRecoverScreen"
    const val RecoveryEnter = "recoveryEnterScreen?${Keys.CONFIRMED_CODE}={${Keys.CONFIRMED_CODE}}"
    fun recoveryEnterPath(code: String) = "recoveryEnterScreen?${Keys.CONFIRMED_CODE}=$code"

    const val Home = "homeScreen"
    const val HomeDeepLink = "$rootUri/homeScreen"

    const val CurrencyDetails = "currencyDetailsScreen/{${Keys.CURRENCY}}"
    fun currencyDetailsPath(currency: CurrencyType) = "currencyDetailsScreen/${currency.shortName}"

    const val RecentRecords = "recentRecordsScreen"
    const val TransactionDetails = "transactionDetailsScreen/{${Keys.CURRENCY}}?${Keys.TRANS_ID}={${Keys.TRANS_ID}}"
    const val TransactionDetailsLink = "$rootUri/$TransactionDetails"
    fun transactionDetailsPath(currency: CurrencyType, transactionId: String) =
        "transactionDetailsScreen/${currency.shortName}?${Keys.TRANS_ID}=$transactionId"
    fun transactionDetailsDeepLink(currency: CurrencyType, transactionId: String) =
        "$rootUri/${transactionDetailsPath(currency, transactionId)}".toUri()

    const val Transfer = "transferScreen"

    const val ReceiveQrCode = "receiveQrCodeScreen"
    const val PrivateKeyWarning = "privateKeyWarningScreen/{${Keys.CURRENCY}}?${Keys.CONFIRMED_CODE}={${Keys.CONFIRMED_CODE}}"
    fun privateKeyWarningPath(code: String, currency: CurrencyType) =
        "privateKeyWarningScreen/${currency.shortName}?${Keys.CONFIRMED_CODE}=$code"
    const val PrivateKey = "privateKeyScreen/{${Keys.CURRENCY}}?${Keys.CONFIRMED_CODE}={${Keys.CONFIRMED_CODE}}"
    fun privateKeyPath(code: String, currency: CurrencyType) =
        "privateKeyScreen/${currency.shortName}?${Keys.CONFIRMED_CODE}=$code"
    const val ShowSeedPhrase = "showSeedPhraseScreen?${Keys.CONFIRMED_CODE}={${Keys.CONFIRMED_CODE}}"
    fun showSeedPhrasePath(code: String) = "showSeedPhraseScreen?${Keys.CONFIRMED_CODE}=$code"

    const val ConfirmPasscode = "confirmPasscodeScreen"

    const val AddressList = "addressListScreen/{${Keys.CURRENCY}}"
    fun addressListPath(currency: CurrencyType) = "addressListScreen/${currency.shortName}"
    const val AddAddress = "addAddressScreen/{${Keys.CURRENCY}}?${Keys.ADDRESS_ID}={${Keys.ADDRESS_ID}}"
    fun addAddressPath(
        currency: CurrencyType,
        addressId: String? = null,
    ) = buildString {
        append("addAddressScreen")
        append("/${currency.shortName}")
        if (addressId != null) {
            append("?${Keys.ADDRESS_ID}=$addressId")
        }
    }
    val AddAddressArguments = listOf(
        navArgument(Keys.ADDRESS_ID) { nullable = true },
    )
    const val ScanQrCode = "scanQrCodeScreen"

    const val GasFee = "gasFeeScreen/{${Keys.CURRENCY}}?${Keys.GAS_PRICES}={${Keys.GAS_PRICES}}" +
            "&${Keys.SELECTED_GAS_FEE}={${Keys.SELECTED_GAS_FEE}}"
    fun gasFeePath(currency: CurrencyType, gasPrices: List<Double>, selectedFee: Double) =
        "gasFeeScreen/${currency.shortName}?${Keys.GAS_PRICES}=${gasPrices.joinToString(",")}" +
                "&${Keys.SELECTED_GAS_FEE}=$selectedFee"
}

internal object Graphs {
    const val Register = "registerGraph"
    const val Recovery = "recoveryGraph"

    const val Transfer = "transferGraph?${Keys.CURRENCY}={${Keys.CURRENCY}}" +
            "&${Keys.OTHER_USER_ID}={${Keys.OTHER_USER_ID}}"
    const val TransferLink = "$rootUri/${Transfer}"
    fun transferPath(currency: CurrencyType, otherUserId: String = "") = "transferGraph?" +
            "${Keys.CURRENCY}=${currency.shortName}&${Keys.OTHER_USER_ID}=$otherUserId"
    fun transferDeepLink(currency: CurrencyType, otherUserId: String) =
        "$rootUri/${transferPath(currency, otherUserId)}".toUri()

    const val Receive = "receiveGraph?${Keys.CURRENCY}={${Keys.CURRENCY}}"
    fun receivePath(currency: CurrencyType) = "receiveGraph?${Keys.CURRENCY}=${currency.shortName}"
}

@Suppress("DEPRECATION")
internal val Bundle.currencyArgument: CurrencyType?
    get() = getString(Keys.CURRENCY)?.fromShortName
