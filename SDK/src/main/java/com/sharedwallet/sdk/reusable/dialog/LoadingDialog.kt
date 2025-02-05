package com.sharedwallet.sdk.reusable.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sharedwallet.sdk.reusable.progress.SmallLoadingProgress

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun LoadingDialog() {
    Dialog(
        onDismissRequest = { /*Do nothing*/ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Surface(
            shape = RoundedCornerShape(15.dp),
        ) {
            Box(modifier = Modifier.padding(32.dp)) {
                SmallLoadingProgress()
            }
        }
    }
}
