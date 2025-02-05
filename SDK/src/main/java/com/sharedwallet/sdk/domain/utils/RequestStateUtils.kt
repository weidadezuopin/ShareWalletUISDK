package com.sharedwallet.sdk.domain.utils

import com.sharedwallet.sdk.domain.state.RequestState

fun List<RequestState<*>>.firstError() = filterIsInstance<RequestState.Error>().firstOrNull()

fun <T> RequestState<T>.successData() = (this as RequestState.Success).data
