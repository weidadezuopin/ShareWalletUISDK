package com.sharedwallet.sdk.reusable.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@Composable
fun SeedPhraseGridCard(
    modifier: Modifier = Modifier,
    content: LazyGridScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        backgroundColor = SwTheme.colors.blueCard,
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .padding(8.dp),
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 3),
                userScrollEnabled = false,
                verticalArrangement = Arrangement.spacedBy(2.dp),
                content = content,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSeedPhrase() {
    SharedWalletTheme {
        SeedPhraseGridCard {

        }
    }
}
