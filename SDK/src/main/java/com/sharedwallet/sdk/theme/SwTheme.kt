package com.sharedwallet.sdk.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.sharedwallet.sdk.theme.colors.SwColors
import com.sharedwallet.sdk.theme.fonts.SwTypography

/**
 * In order to override, this provider must be called at the initiation of your app.
 * Avoid updating this value after initiation, it may involve in side effects.
 */
var SwColorsProvider: () -> SwColors = {
    SwColors()
}

private val LocalSwColors = staticCompositionLocalOf {
    SwColors()
}

@Composable
fun SharedWalletTheme(
    content: @Composable () -> Unit,
) {
    val swColors = SwColorsProvider.invoke()
    CompositionLocalProvider(LocalSwColors provides swColors) {
        MaterialTheme(
            typography = SwTypography,
            colors = lightColors(
                primary = swColors.primary,
                secondary = swColors.secondary,
                onSecondary = Color.White,
            ),
            content = content,
        )
    }
}

object SwTheme {
    val colors: SwColors
        @Composable
        get() = LocalSwColors.current
}
