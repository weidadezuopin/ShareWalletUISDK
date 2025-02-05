package com.sharedwallet.sdk.domain.usecase

import com.sharedwallet.sdk.data.repository.UserRepository
import com.sharedwallet.sdk.di.WalletKoinComponent
import com.sharedwallet.sdk.domain.state.UserState
import org.koin.core.component.inject

internal class UserRegisteredUseCase: WalletKoinComponent {

    private val userRepository: UserRepository by inject()

    suspend operator fun invoke(): Boolean =
        userRepository.getUserState() == UserState.AccountGenerated
}
