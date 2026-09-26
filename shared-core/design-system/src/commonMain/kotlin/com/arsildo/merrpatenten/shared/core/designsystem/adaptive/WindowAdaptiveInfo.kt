package com.arsildo.merrpatenten.shared.core.designsystem.adaptive

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.window.core.layout.WindowSizeClass

enum class DevicePosture {
    FLAT,
    TABLETOP,
    BOOK,
}

val LocalFoldPosture = compositionLocalOf<DevicePosture?> { null }

/**
 * Convenience accessors wrapping the official Material3 adaptive [WindowSizeClass].
 * Consumers call `rememberWindowAdaptiveInfo()` inside any composable.
 */
data class WindowAdaptiveInfo(val windowSizeClass: WindowSizeClass, val posture: DevicePosture = DevicePosture.FLAT) {
    val isCompact: Boolean
        get() = !windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    val isAtLeastMedium: Boolean
        get() = windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    val isTabletop: Boolean
        get() = posture == DevicePosture.TABLETOP
    val isBook: Boolean
        get() = posture == DevicePosture.BOOK
    val isDualPane: Boolean
        get() = isAtLeastMedium || isBook
}

/**
 * Returns a [WindowAdaptiveInfo] combining the official Material3 adaptive window size class
 * with the fold posture from [m3Info.windowPosture] (or overridden via [LocalFoldPosture]).
 */
@Composable
fun rememberWindowAdaptiveInfo(): WindowAdaptiveInfo {
    val m3Info = currentWindowAdaptiveInfoV2()
    val localPosture = LocalFoldPosture.current
    val posture = localPosture ?: when {
        m3Info.windowPosture.isTabletop -> DevicePosture.TABLETOP
        m3Info.windowPosture.hingeList.any { it.isVertical && !it.isFlat } -> DevicePosture.BOOK
        else -> DevicePosture.FLAT
    }
    return WindowAdaptiveInfo(
        windowSizeClass = m3Info.windowSizeClass,
        posture = posture,
    )
}
