package com.sharedwallet.sdk.data.datasource

import android.content.Context
import cold_wallet.Cold_wallet
import com.sharedwallet.sdk.data.models.*
import hot_wallet.Hot_wallet
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

internal class WalletSdkDataSource(
    private val context: Context,
    private val jsonProvider: JsonProvider,
    private val requestHandler: RequestHandler,
) {

    fun version() = Cold_wallet.sdkVersion()

    suspend fun getUserStatus() = requestHandler.handle { Cold_wallet.getUserStatus() }

    suspend fun getSeedPhrase(passcode: String) = requestHandler.handle {
        Cold_wallet.getUserSeedPhrase(passcode)
    }

    suspend fun init(
        clientId: String,
        apiLink: String,
    ) = requestHandler.handle {
        val initialSdkRequest = InitialSdkRequest(
            dataDirectoryPath = context.filesDir.path,
            apiAddress = apiLink,
        )
        Cold_wallet.initWalletSDK(
            clientId,
            jsonProvider.encodeToString(InitialSdkRequest.serializer(), initialSdkRequest),
        )
    }

    suspend fun getCoinStatuses() = requestHandler.handle {
        Hot_wallet.getCoinStatuses()
    }

    suspend fun verifyPasscode(passcode: String) = requestHandler.handle {
        Cold_wallet.verifyPasscode(passcode)
    }

    suspend fun generateUserAccount(passcode: String) = requestHandler.handle {
        val seedPhrase = Cold_wallet.generateUserAccount("en", passcode)
        seedPhrase.split(' ')
    }

    suspend fun fetchSeedWord(query: String) = requestHandler.handle {
        val words = Cold_wallet.fetchSeedPhraseWord(query)
        words.split(' ').filterNot { it.isEmpty() }
    }

    suspend fun recoverUserAccount(passcode: String, seedPhrase: String) = requestHandler.handle {
        Cold_wallet.recoverUserAccount(passcode, seedPhrase)
    }

    suspend fun fetchLocalCoins() = requestHandler.handle {
        jsonProvider.decodeFromString(ListSerializer(CurrencyDto.serializer()), Cold_wallet.fetchLocalCoins())
    }

    suspend fun getCoinRatio() = requestHandler.handle {
        jsonProvider.decodeFromString(ListSerializer(CoinRatioDto.serializer()), Hot_wallet.getCoinRatio())
    }

    suspend fun getBalance(currencyType: Int, publicAddress: String) = requestHandler.handle {
        jsonProvider.decodeFromString(
            Double.serializer(),
            Hot_wallet.getBalance(currencyType.toLong(), publicAddress),
        )
    }

    suspend fun getGasPrice(currencyType: Int) = requestHandler.handle {
        jsonProvider.decodeFromString(
            Double.serializer(),
            Hot_wallet.getGasPrice(currencyType.toLong()),
        )
    }

    suspend fun getTransactionFee(currencyType: Int, gasPrice: Double) = requestHandler.handle {
        jsonProvider.decodeFromString(
            Double.serializer(),
            Hot_wallet.getTransactionFee(currencyType.toLong(), gasPrice),
        )
    }

    suspend fun getPublicAddress(currencyType: Int) = requestHandler.handle {
        jsonProvider.decodeFromString(
            serializer = ListSerializer(PublicAddressDto.serializer()),
            value = Hot_wallet.getPublicAddress(currencyType.toLong()),
        )
    }

    suspend fun getUserPrivateKey(passcode: String, currencyType: Int, compressType: Int) =
        requestHandler.handle {
            Cold_wallet.getUserPrivateKey(passcode, currencyType.toLong(), compressType.toLong())
        }

    suspend fun getLocalUserAddressBook(currencyType: Int) = requestHandler.handle {
        jsonProvider.decodeFromString(
            serializer = ListSerializer(AddressDto.serializer()),
            value = Cold_wallet.getLocalUserAddressBook(currencyType.toLong()),
        )
    }

    suspend fun addAddressBook(currencyType: Int, address: AddressDto) = requestHandler.handle {
        Cold_wallet.addAddressBook(currencyType.toLong(), address.id, address.name)
    }

    suspend fun deleteAddressBook(currencyType: Int, addressId: String) = requestHandler.handle {
        Cold_wallet.deleteAddressBook(currencyType.toLong(), addressId)
    }

    suspend fun transfer(currencyType: Int, publicAddress: String, toAddress: String, passcode: String, amount: Double, gasPrice: Double) = requestHandler.handle {
        Hot_wallet.transfer(currencyType.toLong(), publicAddress, toAddress, passcode, amount, gasPrice)
    }

    suspend fun getTransactionList(
        currencyType: Int,
        publicAddress: String,
        transactionType: Int,
        pageNumber: Int,
        loadSize: Int,
    ) = requestHandler.handle {
        jsonProvider.decodeFromString(
            serializer = RecordListResponse.serializer(),
            value = Hot_wallet.getTransactionList(
                currencyType.toLong(),
                publicAddress,
                transactionType.toLong(),
                pageNumber.toLong(),
                loadSize.toLong(),
                "create_time:desc",
            ),
        )
    }

    suspend fun getTransaction(
        currencyType: Int,
        publicAddress: String,
        transactionHash: String,
    ) = requestHandler.handle {
        jsonProvider.decodeFromString(
            serializer = TransactionDto.serializer(),
            Hot_wallet.getTransaction(currencyType.toLong(), publicAddress, transactionHash),
        )
    }

    suspend fun getRecentTransactions(
        pageNumber: Int,
        loadSize: Int,
    ) = requestHandler.handle {
        jsonProvider.decodeFromString(
            serializer = RecentRecordListResponse.serializer(),
            value = Hot_wallet.getRecentTransactions(
                pageNumber.toLong(),
                loadSize.toLong(),
            ),
        )
    }

    suspend fun fetchFriendAddress(
        currencyType: Int,
        userId: String,
    ) = requestHandler.handle {
        jsonProvider.decodeFromString(
            serializer = String.serializer(),
            value = Hot_wallet.fetchFriendAddress(
                userId,
                currencyType.toLong(),
            ),
        )
    }
}
