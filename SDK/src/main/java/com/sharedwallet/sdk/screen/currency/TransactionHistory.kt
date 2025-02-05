package com.sharedwallet.sdk.screen.currency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.pager.*
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.models.Record
import com.sharedwallet.sdk.domain.utils.isReceived
import com.sharedwallet.sdk.reusable.*
import com.sharedwallet.sdk.temp.fakeRecords
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TransactionHistoryCard(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    allRecords: LazyPagingItems<Record>,
    sentRecords: LazyPagingItems<Record>,
    receivedRecords: LazyPagingItems<Record>,
    onRecordClick: (record: Record) -> Unit,
) {
    Card(
        modifier = modifier,
        elevation = 8.dp,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                text = stringResource(id = R.string.sw_transactions_history),
            )
            RecordsPages(
                pagerState = pagerState,
                allRecords = allRecords,
                sentRecords = sentRecords,
                receivedRecords = receivedRecords,
                onRecordClick = onRecordClick,
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun RecordsPages(
    pagerState: PagerState,
    allRecords: LazyPagingItems<Record>,
    sentRecords: LazyPagingItems<Record>,
    receivedRecords: LazyPagingItems<Record>,
    onRecordClick: (record: Record) -> Unit,
) {
    val scope = rememberCoroutineScope()
    TabRow(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = contentColorFor(MaterialTheme.colors.surface),
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .pagerTabIndicatorOffset(pagerState, tabPositions)
                    .padding(horizontal = 24.dp),
                color = SwTheme.colors.primary,
            )
        },
    ) {
        listOf(
            R.string.sw_all,
            R.string.sw_send,
            R.string.sw_receive
        ).forEachIndexed { index, resId ->
            val isSelected = pagerState.currentPage == index
            Tab(
                text = {
                    Text(
                        text = stringResource(id = resId),
                        style = MaterialTheme.typography.body1,
                        fontSize = 14.sp,
                        color = if (isSelected) SwTheme.colors.primary else Color.Unspecified,
                    )
                },
                selected = isSelected,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
    HorizontalPager(
        modifier = Modifier.fillMaxHeight(),
        count = 3,
        state = pagerState,
        verticalAlignment = Alignment.Top,
    ) { page ->
        when (page) {
            0 -> RecordsList(records = allRecords, onRecordClick = onRecordClick)
            1 -> RecordsList(records = sentRecords, onRecordClick = onRecordClick)
            2 -> RecordsList(records = receivedRecords, onRecordClick = onRecordClick)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RecordsList(
    records: LazyPagingItems<Record>,
    onRecordClick: (record: Record) -> Unit,
) {
    val scrollState: LazyListState = rememberLazyListState()
    LaunchedEffect(key1 = records.loadState.refresh) {
        if (records.loadState.refresh is LoadState.NotLoading) {
            scrollState.scrollToItem(0)
        }
    }

    val refreshing = records.loadState.refresh is LoadState.Loading
    val pullRefreshState = rememberPullRefreshState(refreshing, { records.refresh() })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .clipToBounds(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            state = scrollState,
        ) {
            if (
                records.loadState.append.endOfPaginationReached &&
                records.itemCount <= 0
            ) {
                item {
                    Column(
                        modifier = Modifier.fillParentMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        NoData()
                    }
                }
            } else {
                items(items = records, key = { it.id }) { record ->
                    if (record != null) {
                        RecordItem(
                            record = record,
                            onClick = { onRecordClick(record) },
                        )
                    } else {
                        Text(text = "Null")
                    }
                }
            }

            loadListState(
                loadState = records.loadState.refresh,
                onRefresh = { records.refresh() }
            )

            paginationListState(
                loadState = records.loadState.append,
                onRetry = { records.retry() }
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

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewTransactionHistory() {
    SharedWalletTheme {
        TransactionHistoryCard(
            pagerState = rememberPagerState(),
            allRecords = flowOf(PagingData.from(fakeRecords)).collectAsLazyPagingItems(),
            sentRecords = flowOf(PagingData.from(fakeRecords.filter { it.isSent })).collectAsLazyPagingItems(),
            receivedRecords = flowOf(PagingData.from(fakeRecords.filter { it.isReceived })).collectAsLazyPagingItems(),
            onRecordClick =  { },
        )
    }
}
