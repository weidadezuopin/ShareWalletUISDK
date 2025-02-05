package com.sharedwallet.sdk.screen.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.models.Address
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddressItem(
    modifier: Modifier = Modifier,
    address: Address,
    onCopyClick: () -> Unit = { },
    onClick: () -> Unit = { },
) {
    Card(
        modifier = Modifier.padding(4.dp),
        backgroundColor = SwTheme.colors.blueCard,
        shape = RoundedCornerShape(12.dp),
        elevation = 0.dp,
        onClick = onClick,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            shape = RoundedCornerShape(6.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Column {
                    Text(text = stringResource(id = R.string.sw_name), fontSize = 14.sp)
                    Text(text = stringResource(id = R.string.sw_address), fontSize = 14.sp)
                }
                Spacer(modifier = modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Text(
                        text = address.name,
                        style = MaterialTheme.typography.body2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "${address.id.take(12)}...",
                        style = MaterialTheme.typography.body2,
                        color = SwTheme.colors.clickableText,
                    )
                }
                Spacer(modifier = modifier.width(20.dp))
                IconButton(modifier = Modifier.size(28.dp), onClick = onCopyClick) {
                    SwIcon(iconId = R.drawable.sw_ic_copy)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddressItem() {
    SharedWalletTheme {
        AddressItem(address = Address(id = "0x1ed1fd2esdd", name = "Someone"))
    }
}
