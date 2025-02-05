package com.sharedwallet.sdk.domain.utils

import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

inline fun <reified T> NavController.inCurrentStackStateFlow(key: String): StateFlow<T?> {
    return currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<T?>(key, null) ?: MutableStateFlow(null)
}

fun NavController.removeInCurrentStack(key: String) {
    currentBackStackEntry?.savedStateHandle?.set(key, null)
}

inline fun <reified T> NavController.setInPreviousStack(key: String, value: T) {
    previousBackStackEntry?.savedStateHandle?.set(key, value)
}
