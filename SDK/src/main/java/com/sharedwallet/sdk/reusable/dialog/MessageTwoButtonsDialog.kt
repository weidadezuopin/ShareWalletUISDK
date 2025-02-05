package com.sharedwallet.sdk.reusable.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun MessageTwoButtonsDialog(
    onCancelClick: () -> Unit,
    onOkClick: () -> Unit,
    text: String,
    cancelText: String = stringResource(id = R.string.sw_cancel),
    okText: String = stringResource(id = R.string.sw_yes),
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    AlertDialog(
        modifier = Modifier.width(screenWidth * .8f),
        onDismissRequest = onCancelClick,
        shape = RoundedCornerShape(12.dp),
        text = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
            )
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = onCancelClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = SwTheme.colors.primary),
                ) {
                    Text(text = cancelText)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onOkClick,
                    colors = ButtonDefaults.buttonColors(backgroundColor = SwTheme.colors.primary),
                ) {
                    Text(text = okText)
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewTwoMessageDialog() {
    SharedWalletTheme {
        MessageTwoButtonsDialog(
            onCancelClick = {  },
            onOkClick = {  },
            text = "Some text?",
        )
    }
}
