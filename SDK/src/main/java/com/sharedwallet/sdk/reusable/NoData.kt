package com.sharedwallet.sdk.reusable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.theme.SharedWalletTheme

@Composable
internal fun NoData(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = R.drawable.sw_no_record),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = stringResource(id = R.string.sw_no_data_found),
            color = Color.Black.copy(alpha = .8f),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoData() {
    SharedWalletTheme {
        NoData()
    }
}
