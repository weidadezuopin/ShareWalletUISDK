package com.sharedwallet.sdk.reusable.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.reusable.SwIcon

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeleteCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier =modifier,
        shape = RoundedCornerShape(10.dp),
        elevation = 0.dp,
        backgroundColor = Color.Red,
        contentColor = Color.White,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = stringResource(id = R.string.sw_delete), fontSize = 10.sp)
            Spacer(modifier = Modifier.height(4.dp))
            SwIcon(iconId = R.drawable.sw_ic_delete)
        }
    }
}