package com.sharedwallet.sdk.screen.passcode

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.compose.clickableNoRipple
import com.sharedwallet.sdk.domain.state.PasscodeState
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.ErrorText
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.dialog.InfoDialog
import com.sharedwallet.sdk.reusable.field.PinCodeField
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.viewmodel.EnterPasscodeViewModel

@Composable
internal fun EnterPassCodeScreen(
    viewModel: EnterPasscodeViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onConfirmed: (passcode: String) -> Unit = { },
) {
    val passCode by viewModel.passCodeFlow.collectAsStateWithLifecycle()
    val passcodeState by viewModel.passcodeStateFlow.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoadingFlow.collectAsStateWithLifecycle()

    var showForgotPasswordDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = passcodeState) {
        if (passcodeState is PasscodeState.Confirmed) {
            onConfirmed(passCode.code)
        }
    }

    WalletScaffold(
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_enter_pin_code),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            PinCodeField(
                modifier = Modifier.fillMaxWidth(),
                passCode = passCode,
                onCodeChange = { viewModel.setCode(it) },
                enabled = !isLoading,
                isError = passcodeState is PasscodeState.Error,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                visible = passcodeState is PasscodeState.Error,
            ) {
                ErrorText(text = stringResource(id = R.string.sw_pin_code_wrong))
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(24.dp)
                    .clickableNoRipple(onClick = { showForgotPasswordDialog = true }),
                text = stringResource(id = R.string.sw_forgot_your_password),
            )
        }
    }

    if (showForgotPasswordDialog) {
        InfoDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            text = {
                Text(
                    text = stringResource(id = R.string.sw_forgot_password_note),
                    textAlign = TextAlign.Center,
                )
            },
            actionText = stringResource(id = R.string.sw_got_it),
            onActionClick = { showForgotPasswordDialog = false },
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewEnterPassCode() {
    SharedWalletTheme {
        EnterPassCodeScreen()
    }
}
