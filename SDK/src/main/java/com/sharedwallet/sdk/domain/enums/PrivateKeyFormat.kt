package com.sharedwallet.sdk.domain.enums

enum class PrivateKeyFormat(val key: Int) {
    Hex(1), CompressedWIF(2), UncompressedWIF(3)
}
