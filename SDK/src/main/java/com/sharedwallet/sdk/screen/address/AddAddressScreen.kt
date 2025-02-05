package com.sharedwallet.sdk.screen.address

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.domain.utils.shortNameWithAlis
import com.sharedwallet.sdk.reusable.DefaultActionBar
import com.sharedwallet.sdk.reusable.ErrorText
import com.sharedwallet.sdk.reusable.WalletScaffold
import com.sharedwallet.sdk.reusable.buttons.PrimaryButton
import com.sharedwallet.sdk.reusable.errorMessage
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.viewmodel.AddAddressViewModel

@Composable
internal fun AddAddressScreen(
    currency: CurrencyType,
    viewModel: AddAddressViewModel = koinSwViewModel(),
    selectedAddressId: String? = null,
    onConsumeSelectedAddressRequest: () -> Unit = { },
    onBackClick: () -> Unit = { },
    onScanClick: () -> Unit = { },
    onSaved: () -> Unit = { },
) {
    val addressId by viewModel.addressTextFlow.collectAsStateWithLifecycle()
    val name by viewModel.nameTextFlow.collectAsStateWithLifecycle()
    val isFieldsValid by viewModel.isFieldsValidFlow.collectAsStateWithLifecycle()
    val saveState by viewModel.saveStateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = selectedAddressId) {
        if (selectedAddressId != null) {
            viewModel.addressTextFlow.value = selectedAddressId
            onConsumeSelectedAddressRequest()
        }
    }
    
    LaunchedEffect(key1 = saveState) {
        if (saveState is UiState.Success) {
            onSaved()
        }
    }

    WalletScaffold(
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_add_address, currency.shortNameWithAlis),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(horizontal = 24.dp),
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            AddAddressCard(
                name = name,
                onNameChange = {
                    viewModel.nameTextFlow.value = it
                },
                address = addressId,
                onAddressChange = {
                    viewModel.addressTextFlow.value = it.trim()
                },
                onScanClick = onScanClick,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                visible = saveState is UiState.Error,
            ) {
                val state = saveState
                val errorText = if (state is UiState.Error) {
                    errorMessage(state.e)
                } else {
                    ""
                }
                ErrorText(text = errorText)
            }
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                visible = !isFieldsValid,
            ) {
                ErrorText(text = stringResource(id = R.string.sw_address_and_name_required))
            }
            Spacer(modifier = Modifier.height(40.dp))
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.save() },
                text = stringResource(id = R.string.sw_save),
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAddAddress() {
    SharedWalletTheme {
        AddAddressScreen(currency = CurrencyType.TRX)
    }
}
