package com.sharedwallet.sdk.screen.splash

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.state.UserState
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.progress.SmallLoadingProgress
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme
import com.sharedwallet.sdk.viewmodel.SplashViewModel

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
internal fun SplashScreen(
    viewModel: SplashViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onCreateClick: () -> Unit = { },
    onRecoverClick: () -> Unit = { },
    userAlreadyRegistered: () -> Unit = { },
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val pagerState = rememberPagerState()

    val userState by viewModel.userStateFlow.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoadingFlow.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = userState) {
        if(userState == UserState.AccountGenerated) {
            userAlreadyRegistered()
        }
    }

    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {
            viewModel.resetAutoScroll()
        }
    }
    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.autoChangeFlow.collect {
                if (!pagerState.isScrollInProgress) {
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
                }
            }
        }
    }

    WalletScaffold(
        topBar = {
            DefaultActionBar(onBackClick = onBackClick)
        },
    ) {
        Column {
            Spacer(modifier = Modifier.weight(1f))

            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            HorizontalPager(
                modifier = Modifier.height(screenHeight * .5f),
                state = pagerState,
                count = 2,
            ) { page ->
                if (page == 0) {
                    FirstSplashPage()
                } else {
                    SecondSplashPage()
                }
            }

            AnimatedContent(targetState = isLoading) { isLoading ->
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        SmallLoadingProgress()
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 44.dp, vertical = 8.dp),
                        elevation = 8.dp,
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)) {
                            SplashItem(
                                modifier = Modifier.clickable(onClick = onCreateClick),
                                title = stringResource(id = R.string.sw_create_identity),
                                subtitle = stringResource(id = R.string.sw_to_open_new_multichain_wallets),
                            )
                            Divider()
                            SplashItem(
                                modifier = Modifier.clickable(onClick = onRecoverClick),
                                title = stringResource(id = R.string.sw_recover_identity),
                                subtitle = stringResource(id = R.string.sw_to_access_existing_wallets),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1.4f))

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                inactiveColor = SwTheme.colors.grayButton,
                activeColor = SwTheme.colors.primary,
                indicatorWidth = 28.dp,
                indicatorHeight = 2.dp,
                spacing = 4.dp,
            )
            Spacer(modifier = Modifier.weight(1.1f))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSplash() {
    SharedWalletTheme {
        SplashScreen()
    }
}
