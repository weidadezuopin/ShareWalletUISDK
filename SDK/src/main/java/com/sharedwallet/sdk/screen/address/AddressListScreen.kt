package com.sharedwallet.sdk.screen.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sharedwallet.sdk.R
import com.sharedwallet.sdk.domain.enums.CurrencyType
import com.sharedwallet.sdk.domain.models.Address
import com.sharedwallet.sdk.domain.state.UiState
import com.sharedwallet.sdk.domain.utils.copyToClipboard
import com.sharedwallet.sdk.domain.utils.koinSwViewModel
import com.sharedwallet.sdk.domain.utils.shortNameWithAlis
import com.sharedwallet.sdk.reusable.*
import com.sharedwallet.sdk.reusable.buttons.PrimaryButton
import com.sharedwallet.sdk.reusable.cards.DeleteCard
import com.sharedwallet.sdk.reusable.dialog.MessageTwoButtonsDialog
import com.sharedwallet.sdk.reusable.progress.SmallLoadingProgress
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.viewmodel.AddressListViewModel

@Composable
internal fun AddressListScreen(
    currency: CurrencyType,
    viewModel: AddressListViewModel = koinSwViewModel(),
    onBackClick: () -> Unit = { },
    onAddClick: () -> Unit = { },
    onAddressClick: (address: Address) -> Unit = { },
) {
    val context = LocalContext.current
    val addressState by viewModel.addressListFlow.collectAsStateWithLifecycle()
    var addressToDelete by remember { mutableStateOf<Address?>(null) }

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadList()
    }

    WalletScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultActionBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.sw_address_book_of, currency.shortNameWithAlis),
            )
        },
    ) {
        Column {
            Spacer(modifier = Modifier.height(32.dp))
            when (val state = addressState) {
                is UiState.Error -> {
                    Spacer(modifier = Modifier.weight(1f))
                    ErrorText(text = state.e.message ?: "Error")
                    Spacer(modifier = Modifier.weight(1f))
                }
                UiState.Loading -> {
                    SmallLoadingProgress(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        Spacer(modifier = Modifier.weight(1f))
                        NoData(modifier = Modifier.align(Alignment.CenterHorizontally))
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .weight(1f),
                        ) {
                            items(items = state.data, key = { it.id }) { address ->
                                var isExpanded: Boolean by remember { mutableStateOf(false) }
                                SwipeableEnd(
                                    isExpanded = isExpanded,
                                    actionSpaceWidth = 84.dp,
                                    onExpand = { isExpanded = true },
                                    onCollapse = { isExpanded = false },
                                    background = {
                                        DeleteCard(
                                            modifier = Modifier
                                                .height(92.dp)
                                                .fillMaxWidth()
                                                .padding(4.dp),
                                            onClick = {
                                                isExpanded = false
                                                addressToDelete = address
                                            },
                                        )
                                    },
                                ) {
                                    AddressItem(
                                        address = address,
                                        onCopyClick = {
                                            context.copyToClipboard(address.id)
                                            viewModel.tryToast(context.getString(R.string.sw_copied))
                                        },
                                        onClick = { onAddressClick(address) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                onClick = onAddClick,
                text = stringResource(id = R.string.sw_add),
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (addressToDelete != null) {
        MessageTwoButtonsDialog(
            onCancelClick = { addressToDelete = null },
            onOkClick = {
                viewModel.deleteAddress(addressToDelete!!)
                addressToDelete = null
            },
            text = stringResource(id = R.string.sw_delete_this_address),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAddressList() {
    SharedWalletTheme {
        AddressListScreen(currency = CurrencyType.TRX)
    }
}
