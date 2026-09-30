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
 * Consumers call `rememberAppAdaptiveInfo()` or `rememberWindowAdaptiveInfo()` inside any composable.
 */
data class AppAdaptiveInfo(val windowSizeClass: WindowSizeClass, val posture: DevicePosture = DevicePosture.FLAT) {
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

    /**
     * Symmetrical (50/50) split on vertical fold (BOOK posture) to prevent the hinge/crease
     * from splitting through content. Tablets / desktops use [defaultPrimaryWeight].
     */
    fun dualPaneSplitWeights(defaultPrimaryWeight: Float = 0.58f): Pair<Float, Float> = if (isBook) {
        0.5f to 0.5f
    } else {
        defaultPrimaryWeight to (1f - defaultPrimaryWeight)
    }
}

typealias WindowAdaptiveInfo = AppAdaptiveInfo

/**
 * Returns an [AppAdaptiveInfo] combining the official Material3 adaptive window size class
 * with the fold posture from [m3Info.windowPosture] (or overridden via [LocalFoldPosture]).
 */
@Composable
fun rememberAppAdaptiveInfo(): AppAdaptiveInfo {
    val m3Info = currentWindowAdaptiveInfoV2()
    val localPosture = LocalFoldPosture.current
    val posture = localPosture ?: when {
        m3Info.windowPosture.isTabletop -> DevicePosture.TABLETOP
        m3Info.windowPosture.hingeList.any { it.isVertical && !it.isFlat } -> DevicePosture.BOOK
        else -> DevicePosture.FLAT
    }
    return AppAdaptiveInfo(
        windowSizeClass = m3Info.windowSizeClass,
        posture = posture,
    )
}

@Composable
fun rememberWindowAdaptiveInfo(): WindowAdaptiveInfo = rememberAppAdaptiveInfo()
