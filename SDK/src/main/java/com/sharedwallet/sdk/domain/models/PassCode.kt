package com.sharedwallet.sdk.domain.models

data class PassCode(
    val codeList: List<Int?> = MutableList(SIZE) { null }.toList(),
) {

    val code: String
        get() = codeList.filterNotNull().joinToString(separator = "") { it.toString() }

    val isFilled: Boolean
        get() = codeList.all { it != null }

    companion object {
        const val SIZE = 6
    }
}
