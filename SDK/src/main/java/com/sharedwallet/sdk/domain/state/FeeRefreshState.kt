package com.sharedwallet.sdk.domain.state

sealed class FeeRefreshState {
    object Loading : FeeRefreshState()
    class CountDown(val sec: Int): FeeRefreshState()
}
