package com.sharedwallet.sdk.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.RecordState
import com.sharedwallet.sdk.domain.models.Record
import com.sharedwallet.sdk.domain.utils.isReceived
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.domain.utils.plusOrMinus
import com.sharedwallet.sdk.domain.utils.recentSendStateDrawableRes
import com.sharedwallet.sdk.reusable.*
import com.sharedwallet.sdk.reusable.cards.GrayCard
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme
import com.sharedwallet.sdk.viewmodel.RecentRecordsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun RecentRecordsScreen(
    viewModel: RecentRecordsViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onRecordClick: (record: Record) -> Unit = { },
) {
    val allRecords = viewModel.recordsFlow.collectAsLazyPagingItems()

    val refreshing = allRecords.loadState.refresh is LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(refreshing, { allRecords.refresh() })

    WalletScaffold(
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_recent_records),
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState),
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {

                items(items = allRecords) { record ->
                    if (record != null) {
                        RecentRecordItem(
                            record = record,
                            onClick = { onRecordClick(record) },
                        )
                    }
                }

                loadListState(
                    loadState = allRecords.loadState.refresh,
                    onRefresh = { allRecords.refresh() }
                )

                paginationListState(
                    loadState = allRecords.loadState.append,
                    onRetry = { allRecords.retry() }
                )
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = refreshing,
                state = pullRefreshState,
                contentColor = MaterialTheme.colors.primary,
            )
        }
    }
}

@Composable
fun RecentRecordItem(
    record: Record,
    onClick: () -> Unit = { },
) {
    GrayCard(
        modifier = Modifier.padding(4.dp),
        elevation = 4.dp,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (record.state == RecordState.Failed) {
                SwCircleIcon(
                    size = 46.dp,
                    iconId = R.drawable.sw_ic_record_failed,
                    color = SwTheme.colors.failed.copy(alpha = .2f),
                )
            } else {
                SwCircleIcon(
                    size = 46.dp,
                    iconId = record.recentSendStateDrawableRes,
                    color = SwTheme.colors.succeed.copy(alpha = .06f),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = "%s %s".format(
                    stringResource(
                        id = when {
                            record.isSent && record.state == RecordState.Failed -> R.string.sw_sent_failed
                            record.isReceived && record.state == RecordState.Failed -> R.string.sw_received_failed
                            record.isSent -> R.string.sw_sent
                            else -> R.string.sw_received
                        },
                    ),
                    record.currency.shortName,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${record.plusOrMinus}${record.amount}",
                    style = MaterialTheme.typography.body2,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = record.date,
                    style = MaterialTheme.typography.h2,
                    fontSize = 12.sp,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            SwCircleIcon(
                size = 28.dp,
                iconId = R.drawable.sw_ic_arrow_forward,
                color = SwTheme.colors.grayButton,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewRecentRecords() {
    SharedWalletTheme {
        RecentRecordsScreen()
    }
}
