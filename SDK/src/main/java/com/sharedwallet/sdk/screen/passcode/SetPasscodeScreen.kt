package com.sharedwallet.sdk.screen.passcode

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.SetPasscodeState
import com.sharedwallet.sdk.domain.state.PasscodeState
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.ErrorText
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.field.PinCodeField
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme
import com.sharedwallet.sdk.viewmodel.SetPasscodeViewModel

@Composable
internal fun SetPasscodeScreen(
    viewModel: SetPasscodeViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onPasscodeConfirmed: (code: String) -> Unit = { },
) {
    val passcode by viewModel.passCodeUiFlow.collectAsStateWithLifecycle()
    val passcodeState by viewModel.passcodeStateFlow.collectAsStateWithLifecycle()
    val setPasscodeState by viewModel.setPasscodeStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = passcodeState) {
        if (passcodeState is PasscodeState.Confirmed) {
            onPasscodeConfirmed((passcodeState as PasscodeState.Confirmed).passCode.code)
        }
    }

    WalletScaffold(
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_set_pin_code),
            )
        },
    ) {
        Column {
            Spacer(modifier = Modifier.height(28.dp))
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                visible = setPasscodeState == SetPasscodeState.Confirm,
            ) {
                Text(
                    text = stringResource(id = R.string.sw_enter_password_again),
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            PinCodeField(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                passCode = passcode,
                onCodeChange = { viewModel.setCode(it) },
                isError = passcodeState is PasscodeState.Error,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                visible = passcodeState is PasscodeState.Error,
            ) {
                ErrorText(text = stringResource(id = R.string.sw_pin_code_not_match))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = SwTheme.colors.primary)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = SwTheme.colors.primary.copy(alpha = .1f)),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.sw_password_used_info),
                    color = SwTheme.colors.primary,
                    fontSize = 12.sp,
                )
            }
            Divider(color = SwTheme.colors.primary)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSetPasscode() {
    SharedWalletTheme {
        SetPasscodeScreen()
    }
}
