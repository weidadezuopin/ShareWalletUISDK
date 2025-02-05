package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.ViewModel
import com.sharedwallet.sdk.data.repository.ToastRepository
import com.sharedwallet.sdk.data.repository.UserRepository
import com.sharedwallet.sdk.domain.state.UserState
import kotlinx.coroutines.runBlocking

internal class MainViewModel(
    toastRepository: ToastRepository,
    private val userRepository: UserRepository,
): ViewModel() {

    val toastFlow = toastRepository.toastFlow

    fun isUserLoggedIn() = runBlocking {
        userRepository.getUserState() == UserState.AccountGenerated
    }
}
