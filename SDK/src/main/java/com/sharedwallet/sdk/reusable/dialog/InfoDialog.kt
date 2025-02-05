package com.sharedwallet.sdk.reusable.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun InfoDialog(
    onDismissRequest: () -> Unit,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    actionText: String? = null,
    onActionClick: () -> Unit = { },
    properties: DialogProperties = DialogProperties(),
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Surface(
            modifier = Modifier.width(screenWidth * .8f),
            shape = RoundedCornerShape(15.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                if (title != null) {
                    title()
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (text != null) {
                    text()
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (actionText != null) {
                    TextButton(onClick = onActionClick) {
                        Text(
                            text = actionText,
                            style = MaterialTheme.typography.h2,
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewInfoDialog() {

}
