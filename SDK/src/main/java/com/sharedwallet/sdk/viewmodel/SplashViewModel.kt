package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharedwallet.sdk.data.repository.InitSdkRepository
import com.sharedwallet.sdk.data.repository.UserRepository
import com.sharedwallet.sdk.domain.state.UserState
import com.sharedwallet.sdk.domain.utils.tickerFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

internal class SplashViewModel(
    private val initSdkRepository: InitSdkRepository,
    private val userRepository: UserRepository,
): ViewModel() {

    private val _userStateFlow = MutableStateFlow<UserState?>(null)
    val userStateFlow = _userStateFlow.asStateFlow()

    private val _isLoadingFlow = MutableStateFlow(true)
    val isLoadingFlow = _isLoadingFlow.asStateFlow()

    private val resetAutoSplashFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val autoChangeFlow = resetAutoSplashFlow
        .onStart { emit(Unit) }
        .flatMapLatest {
            tickerFlow(5.seconds, 5.seconds)
        }

    fun resetAutoScroll() {
        resetAutoSplashFlow.tryEmit(Unit)
    }

    init {
        viewModelScope.launch {
            _isLoadingFlow.value = true
            initSdkRepository.syncCoins()
            _userStateFlow.update { userRepository.getUserState() }
            _isLoadingFlow.value = false
        }
    }
}
