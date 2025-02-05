package com.sharedwallet.sdk.reusable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import com.sharedwallet.sdk.R
import kotlinx.serialization.SerializationException

@Composable
@ReadOnlyComposable
fun errorMessage(e: Throwable?): String {
    return when(e) {
        is SerializationException -> stringResource(id = R.string.sw_something_went_wrong)
        else -> when {
            e?.message.isNullOrEmpty() -> stringResource(id = R.string.sw_unknown_error)
            e!!.message!!.startsWith("3014") -> stringResource(id = R.string.sw_sending_address_incorrect)
            e.message!!.startsWith("3015") -> stringResource(id = R.string.sw_receiver_address_incorrect)
            e.message!!.startsWith("3016") -> stringResource(id = R.string.sw_address_already_exists)
            e.message!!.startsWith("3012") -> stringResource(id = R.string.sw_phrase_bound_by_others)
            e.message!!.startsWith("3013") -> stringResource(id = R.string.transaction_is_failed)
            e.message!!.startsWith("3017") -> stringResource(id = R.string.sw_network_error)
            e.message!!.startsWith("3018") -> stringResource(id = R.string.sw_address_incorrect)
            e.message!!.startsWith("3100") -> stringResource(id = R.string.sw_create_transaction_failed)
            else -> e.message!!
        }
    }
}

internal val isChineseLang: Boolean =
    Locale.current.language == "zh"
