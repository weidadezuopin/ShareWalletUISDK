package com.sharedwallet.sdk.temp

import com.sharedwallet.sdk.data.mappers.toSeedWord
import com.sharedwallet.sdk.domain.models.SeedWord

val fakeSeedPhrase = listOf("Start", "Text", "Car", "Soup", "Install"
        , "Have", "Android", "Is", "Best", "Action", "Hope", "End")
    .map { it.toSeedWord() }

val fakeShuffledSeedPhrase: List<SeedWord>
    get() = fakeSeedPhrase.shuffled()
