package com.arsildo.merrpatenten.shared.core.designsystem.adaptive

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset

private val StartToolbarClearanceAnimationSpec = spring(
    stiffness = Spring.StiffnessMediumLow,
    visibilityThreshold = Dp.VisibilityThreshold,
)

/**
 * Animates the start clearance for the floating navigation rail on non-compact screens.
 *
 * Returns [State] instead of an unwrapped [Dp] so callers can defer reading [State.value]
 * to the Layout phase via [Modifier.startPadding], avoiding per-frame recomposition of the
 * screen scaffold during window size transitions.
 */
@Composable
fun rememberStartToolbarClearance(isCompact: Boolean): State<Dp> = animateDpAsState(
    targetValue = if (!isCompact) 88.dp else 0.dp,
    animationSpec = StartToolbarClearanceAnimationSpec,
    label = "StartToolbarClearance",
)

/**
 * Applies start padding while deferring the [padding] read to the Layout phase.
 *
 * Use with animated [State] values (such as [rememberStartToolbarClearance]) so frame updates
 * trigger layout and draw only instead of recomposing the annotated composable.
 */
fun Modifier.startPadding(padding: () -> Dp): Modifier = layout { measurable, constraints ->
    val startPx = padding().roundToPx().coerceAtLeast(0)
    val placeable = measurable.measure(constraints.offset(horizontal = -startPx))
    val width = constraints.constrainWidth(placeable.width + startPx)
    val height = constraints.constrainHeight(placeable.height)
    layout(width, height) {
        placeable.placeRelative(startPx, 0)
    }
}
