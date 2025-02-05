package com.sharedwallet.sdk.theme.fonts

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.R

internal val Titillium = FontFamily(
    Font(R.font.titillium_web_regular),
    Font(R.font.titillium_web_light, FontWeight.W300),
    Font(R.font.titillium_web_semi_bold, FontWeight.W600),
    Font(R.font.titillium_web_bold, FontWeight.Bold),
)

internal val SwTypography = Typography(
    defaultFontFamily = Titillium,
    h2 = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
    ),
)

val Typography.custom: TextStyle
    get() = TextStyle(
        fontFamily = Titillium,
    )

