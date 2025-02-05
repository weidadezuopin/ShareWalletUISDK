package com.sharedwallet.sdk.screen.receive

import androidx.activity.compose.BackHandler
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.copyToClipboard
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.SwIcon
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.buttons.PrimaryButton
import com.sharedwallet.sdk.reusable.cards.GrayCard
import com.sharedwallet.sdk.reusable.progress.SmallLoadingProgress
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.viewmodel.ReceiveQrCodeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun ReceiveQrCodeScreen(
    currency: CurrencyType,
    confirmedPasscode: String? = null,
    viewModel: ReceiveQrCodeViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onPasscodeRequest: () -> Unit = { },
    onConsumePasscodeRequest: () -> Unit = { },
    onPrivateKeyRequest: (passcode: String) -> Unit = { },
    onShowSeedPhraseRequest: (passcode: String) -> Unit = { },
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val addressId by viewModel.addressIdFlow.collectAsStateWithLifecycle()
    val qrCodeBitmap by viewModel.qrCodeBitmapFlow.collectAsStateWithLifecycle()

    var nextDirection by rememberSaveable { mutableStateOf<SecureDirection?>(null) }

    val scaffoldState = rememberScaffoldState()

    BackHandler(enabled = bottomSheetState.isVisible) {
        coroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    LaunchedEffect(key1 = confirmedPasscode) {
        if (confirmedPasscode != null) {
            onConsumePasscodeRequest()
            when(nextDirection) {
                SecureDirection.PrivateKey -> onPrivateKeyRequest(confirmedPasscode)
                SecureDirection.SeedPhrase -> onShowSeedPhraseRequest(confirmedPasscode)
                null -> { /* Do nothing */ }
            }
        }
    }

    WalletScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_currency_address, currency.shortName),
            ) {
                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = {
                        coroutineScope.launch { bottomSheetState.show() }
                    },
                ) {
                    SwIcon(iconId = R.drawable.sw_ic_menu)
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.size(240.dp),
                contentAlignment = Alignment.Center,
            ) {
                androidx.compose.animation.AnimatedVisibility(visible = qrCodeBitmap is UiState.Loading) {
                    SmallLoadingProgress()
                }
                val state = qrCodeBitmap
                androidx.compose.animation.AnimatedVisibility(
                    visible = qrCodeBitmap is UiState.Success,
                    enter = fadeIn(),
                ) {
                    if (state is UiState.Success) {
                        Image(
                            modifier = Modifier.size(240.dp),
                            bitmap = state.data.asImageBitmap(),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = "Qr code",
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            GrayCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 20.dp)
                        .align(Alignment.Center),
                    text = addressId,
                    style = MaterialTheme.typography.body2,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                onClick = {
                    context.copyToClipboard(addressId)
                    viewModel.tryToast(context.getString(R.string.sw_copied))
                },
                text = stringResource(id = R.string.sw_copy_address),
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
    ReceiveBottomSheet(
        sheetState = bottomSheetState,
        onCloseClick = { coroutineScope.launch { bottomSheetState.hide() } },
        onPrivateKeyClick = {
            nextDirection = SecureDirection.PrivateKey
            coroutineScope.launch { bottomSheetState.snapTo(ModalBottomSheetValue.Hidden) }
            onPasscodeRequest()
        },
        onShowSeedPhraseClick = {
            nextDirection = SecureDirection.SeedPhrase
            coroutineScope.launch { bottomSheetState.snapTo(ModalBottomSheetValue.Hidden) }
            onPasscodeRequest()
        },
    )
}

private enum class SecureDirection { PrivateKey, SeedPhrase }

@Preview
@Composable
fun PreviewReceiveQrCode() {
    SharedWalletTheme {
        ReceiveQrCodeScreen(currency = CurrencyType.TRX)
    }
}
