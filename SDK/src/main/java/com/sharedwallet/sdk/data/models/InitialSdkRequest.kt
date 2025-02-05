package com.sharedwallet.sdk.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class InitialSdkRequest(
    @SerialName("platform") val platform: Int = 1,
    @SerialName("api_addr") val apiAddress: String = "http://devwalletapi.ddns.net:81",
    @SerialName("data_dir") val dataDirectoryPath: String,
    @SerialName("log_level") val logLevel: Int = 1,
//    @SerialName("api_addr") val apiAddress: String = "http://api.sharewallet-test.com",
)
