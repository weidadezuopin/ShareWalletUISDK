package com.sharedwallet.sdk.domain.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.compose.koinViewModel

@Composable
internal inline fun <reified T : ViewModel> koinSwViewModel() =
    if (LocalInspectionMode.current) {
        // If compose in preview mode
        viewModel()
    } else {
        koinViewModel<T>(
            scope = (LocalContext.current as AndroidScopeComponent).scope,
        )
    }
