package com.sharedwallet.sdk.domain.usecase

import com.sharedwallet.sdk.data.repository.UserRepository
import com.sharedwallet.sdk.di.WalletKoinComponent
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.state.RequestState
import org.koin.core.component.inject

internal class OtherUserRegisteredUseCase(
    private val userId: String,
    private val currencyType: CurrencyType,
): WalletKoinComponent {

    private val userRepository: UserRepository by inject()

    suspend operator fun invoke(): RequestState<Boolean> {
        return when (val request = userRepository.getPublicAddressForUser(currencyType, userId)) {
            is RequestState.Error -> RequestState.Error(request.e)
            is RequestState.Success -> RequestState.Success(request.data.isNotEmpty())
        }
    }

}
