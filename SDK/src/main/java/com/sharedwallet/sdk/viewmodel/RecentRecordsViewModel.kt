package com.sharedwallet.sdk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sharedwallet.sdk.data.repository.RecentRecordsRepository

internal class RecentRecordsViewModel(
    recordsRepository: RecentRecordsRepository,
) : ViewModel() {

    val recordsFlow = recordsRepository.getRecords()
        .cachedIn(viewModelScope)
}
