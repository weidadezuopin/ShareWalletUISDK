package com.sharedwallet.sdk.reusable

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun SwipeableEnd(
    isExpanded: Boolean,
    actionSpaceWidth: Dp,
    onExpand: () -> Unit = { },
    onCollapse: () -> Unit = { },
    background: @Composable (BoxScope.() -> Unit),
    content: @Composable (BoxScope.() -> Unit),
) {
    val thresholds = with(LocalDensity.current) { -actionSpaceWidth.toPx() }
    val scope = rememberCoroutineScope()
    val offset = remember { Animatable(Offset(0f, 0f), Offset.VectorConverter) }

    LaunchedEffect(key1 = isExpanded) {
        val x = if (isExpanded) thresholds else 0f
        offset.animateTo(Offset(x, 0f))
    }

    Box {
        if (offset.value.x < 0f) {
            Box(modifier = Modifier
                .fillMaxHeight()
                .width(actionSpaceWidth)
                .align(Alignment.CenterEnd)) {
                background()
            }
        }
        Box(
            modifier = Modifier
                .offset { offset.value.toIntOffset() }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                val newExpandState = offset.value.x < thresholds * .5f
                                if (newExpandState) onExpand() else onCollapse()
                                offset.animateTo(Offset(if (newExpandState) thresholds else 0f, 0f))
                            }
                        }
                    ) { change, dragAmount ->
                        val summed = offset.value + Offset(x = dragAmount, y = 0f)
                        val newValue =
                            Offset(x = summed.x.coerceIn(thresholds, 0f), y = 0f)
                        change.consume()
                        scope.launch { offset.snapTo(newValue) }
                    }
                },
            content = content,
        )
    }
}

private fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())
