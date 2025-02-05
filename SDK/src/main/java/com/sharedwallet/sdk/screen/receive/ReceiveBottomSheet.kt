package com.sharedwallet.sdk.screen.receive

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReceiveBottomSheet(
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Expanded),
    onPrivateKeyClick: () -> Unit = { },
    onShowSeedPhraseClick: () -> Unit = { },
    onCloseClick: () -> Unit = { },
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetContent = {
            Column {
                Spacer(modifier = Modifier.height(32.dp))
                Item(
                    text = stringResource(id = R.string.sw_display_private_key),
                    onClick = onPrivateKeyClick,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Item(
                    text = stringResource(id = R.string.sw_show_seed_phrase),
                    onClick = onShowSeedPhraseClick,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .padding(horizontal = 36.dp)
                        .fillMaxWidth()
                        .height(54.dp),
                    onClick = onCloseClick,
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.LightGray,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.sw_close),
                        style = MaterialTheme.typography.body2,
                        fontSize = 20.sp,
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    ) {}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Item(
    text: String,
    onClick: () -> Unit = { },
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        onClick = onClick,
        backgroundColor = SwTheme.colors.blueCard,
        shape = RoundedCornerShape(12.dp),
        elevation = 0.dp,
    ) {
        Text(
            modifier = Modifier.padding(20.dp),
            text = text,
            fontSize = 14.sp,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewReceiveBottomSheet() {
    SharedWalletTheme {
        ReceiveBottomSheet()
    }
}
