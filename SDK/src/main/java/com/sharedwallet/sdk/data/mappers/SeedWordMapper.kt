package com.sharedwallet.sdk.data.mappers

import com.sharedwallet.sdk.domain.models.SeedWord
import java.util.*

fun String.toSeedWord() = SeedWord(id = UUID.randomUUID(), word = this)
