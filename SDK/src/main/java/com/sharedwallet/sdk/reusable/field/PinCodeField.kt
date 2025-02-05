package com.sharedwallet.sdk.reusable.field

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sharedwallet.sdk.domain.models.PassCode
import com.sharedwallet.sdk.theme.SharedWalletTheme
import com.sharedwallet.sdk.theme.SwTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PinCodeField(
    modifier: Modifier = Modifier,
    passCode: PassCode,
    onCodeChange: (PassCode) -> Unit,
    enabled: Boolean = true,
    isError: Boolean = false,
) {
    val focusRequestList = remember { MutableList(PassCode.SIZE) { FocusRequester() }.toList() }

    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        focusRequestList.forEachIndexed { index, focusRequester ->
            OtpChar(
                modifier = Modifier
                    .size(50.dp)
                    .focusRequester(focusRequester)
                    .focusProperties {
                        next = focusRequestList.getOrNull(index + 1) ?: focusRequester
                        previous = focusRequestList.getOrNull(index - 1) ?: focusRequester
                    }
                    .onFocusEvent { focusState ->
                        val indexToFocus = if (passCode.isFilled) {
                            passCode.codeList.lastIndex
                        } else {
                            passCode.codeList.indexOfFirst { it == null }
                        }

                        if (focusState.isFocused && index != indexToFocus) {
                            focusRequestList[indexToFocus].requestFocus()
                        } else {
                            focusRequestList[index].freeFocus()
                        }
                    }
                    .onKeyEvent {
                        if (passCode.codeList[index] == null && index > 0 && it.key == Key.Backspace) {
                            onCodeChange(
                                passCode.copy(
                                    codeList = passCode.codeList
                                        .toMutableList()
                                        .apply {
                                            this[index - 1] = null
                                        }
                                )
                            )
                            focusManager.moveFocus(FocusDirection.Previous)
                        } else {
                            false
                        }
                    },
                text = passCode.codeList[index]?.toString() ?: "",
                onDigitChange = {
                    onCodeChange(
                        passCode.copy(
                            codeList = passCode.codeList.toMutableList().apply { this[index] = it }
                        )
                    )
                },
                enabled = enabled,
                isError = isError,
            )
        }
    }

    LaunchedEffect(key1 = enabled) {
        if (enabled) {
            focusRequestList.first().requestFocus()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun OtpChar(
    modifier: Modifier = Modifier,
    text: String,
    onDigitChange: (Int?) -> Unit,
    charVisible: Boolean = false,
    enabled: Boolean = true,
    isError: Boolean = false,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = SwTheme.colors.primary,
        unfocusedBorderColor = SwTheme.colors.grayButton,
    ),
){
    val interactionSource = remember { MutableInteractionSource() }
    val state by remember(text) {
        mutableStateOf(TextFieldValue(text = text, selection = TextRange(text.length)))
    }

    BasicTextField(
        modifier = modifier,
        value = state,
        onValueChange = {
            val newText = it.text.trim()
            val newChar = if (newText.length == 2) newText[1] else newText.firstOrNull()
            onDigitChange(newChar?.digitToIntOrNull())
        },
        singleLine = true,
        visualTransformation = if (charVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation('*')
        },
        textStyle = LocalTextStyle.current.copy(
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
        ),
        enabled = enabled,
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.None,
        ),
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = state.text,
                innerTextField = innerTextField,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                enabled = enabled,
                isError = isError,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                colors = colors,
                border = {
                    TextFieldDefaults.BorderBox(
                        enabled,
                        isError,
                        interactionSource,
                        colors,
                        shape,
                        focusedBorderThickness = 2.dp,
                        unfocusedBorderThickness = 2.dp,
                    )
                },
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPinCodeField() {
    SharedWalletTheme {
        PinCodeField(
            passCode = PassCode(),
            onCodeChange = { },
        )
    }
}
