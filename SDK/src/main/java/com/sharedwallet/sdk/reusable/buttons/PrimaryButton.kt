package com.sharedwallet.sdk.reusable.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
) {
    Button(
        modifier = modifier.height(54.dp),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(27.dp),
        colors = ButtonDefaults.buttonColors(
            disabledBackgroundColor = SwTheme.colors.primary.copy(alpha = .3f),
            disabledContentColor = Color.White.copy(alpha = .5f),
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            fontSize = 20.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainButton() {
    SharedWalletTheme {
        PrimaryButton(onClick = { }, text = "Test button")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainButtonDisabled() {
    SharedWalletTheme {
        PrimaryButton(onClick = { }, text = "Test button", enabled = false)
    }
}
