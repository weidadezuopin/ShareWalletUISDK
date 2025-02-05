package com.sharedwallet.sdk.domain.utils

import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.PrivateKeyFormat

val PrivateKeyFormat.stringRes: Int
    get() = when(this) {
        PrivateKeyFormat.Hex -> R.string.sw_format_hex
        PrivateKeyFormat.CompressedWIF -> R.string.sw_format_compressed_wif
        PrivateKeyFormat.UncompressedWIF -> R.string.sw_format_uncompressed_wif
    }
