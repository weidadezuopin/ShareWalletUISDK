package com.sharedwallet.sdk.reusable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.reusable.progress.SmallLoadingProgress

fun LazyListScope.loadListState(
    loadState: LoadState,
    onRefresh: () -> Unit,
) {
    when (loadState) {
        is LoadState.Error -> {
            item {
                Column(
                    modifier = Modifier.fillParentMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    ErrorText(
                        text = errorMessage(e = loadState.error),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRefresh) {
                        Text(text = stringResource(id = R.string.sw_try_again))
                    }
                }
            }
        }
        else -> {}
    }
}

fun LazyListScope.paginationListState(
    loadState: LoadState,
    onRetry: () -> Unit,
) {
    when (loadState) {
        is LoadState.Error -> {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ErrorText(
                        text = errorMessage(e = loadState.error),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) {
                        Text(text = stringResource(id = R.string.sw_try_again))
                    }
                }
            }
        }
        is LoadState.Loading -> {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SmallLoadingProgress(modifier = Modifier.padding(8.dp))
                }
            }
        }
        else -> {}
    }
}
