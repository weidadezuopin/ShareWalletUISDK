package com.sharedwallet.sdk.screen.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.reusable.SwCircleIcon
import com.sharedwallet.sdk.reusable.field.SwTextField
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun AddAddressCard(
    modifier: Modifier = Modifier,
    name: String,
    onNameChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    onScanClick: () -> Unit = { },
) {
    Card(
        modifier = modifier,
        backgroundColor = SwTheme.colors.blueCard,
        shape = RoundedCornerShape(12.dp),
        elevation = 0.dp,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = MaterialTheme.typography.body1.toSpanStyle()
                            .copy(color = Color.Red),
                    ) {
                        append("*")
                    }
                    withStyle(
                        style = MaterialTheme.typography.body1.toSpanStyle(),
                    ) {
                        append(stringResource(id = R.string.sw_name))
                    }
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                AddAddressField(
                    value = name,
                    onValueChange = onNameChange,
                    length = 20,
                    hint = stringResource(id = R.string.sw_20_char_limit),
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = MaterialTheme.typography.body1.toSpanStyle()
                            .copy(color = Color.Red),
                    ) {
                        append("*")
                    }
                    withStyle(
                        style = MaterialTheme.typography.body1.toSpanStyle(),
                    ) {
                        append(stringResource(id = R.string.sw_address))
                    }
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AddAddressField(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        value = address,
                        onValueChange = onAddressChange,
                        length = 42,
                        hint = stringResource(id = R.string.sw_enter_the_address),
                    )
                    IconButton(onClick = onScanClick) {
                        SwCircleIcon(
                            size = 32.dp,
                            iconId = R.drawable.sw_ic_scan,
                            color = SwTheme.colors.blueCard,
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
private fun AddAddressField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    length: Int = Int.MAX_VALUE,
    hint: String,
) {
    SwTextField(
        modifier = modifier
            .padding(horizontal = 8.dp),
        value = value,
        onValueChange = onValueChange,
        length = length,
        singleLine = true,
        placeholder = {
            Text(
                modifier = Modifier.padding(4.dp),
                text = hint,
                color = SwTheme.colors.placeholder,
            )
        },
    )
}

@Preview
@Composable
fun PreviewAddAddressCard() {
    SharedWalletTheme {
        AddAddressCard(
            name = "",
            onNameChange = {},
            address = "",
            onAddressChange = {},
        )
    }
}
