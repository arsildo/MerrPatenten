package com.arsildo.merrpatenten.shared.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toPath
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleCheck
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CloudExclamationPoint
import com.arsildo.merrpatenten.shared.core.designsystem.icons.SearchX
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Star
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Visual tone for [ExpressiveEmptyState], mapping each mood to a curated
 * Material 3 Expressive [MaterialShapes] morph sequence and tonal color pair.
 */
enum class EmptyStateTone {
    Default,
    Celebratory,
    Search,
    Notice,
}

/**
 * Material 3 Expressive animated empty state with choreographed spring entrance,
 * a continuously morphing [MaterialShapes] hero badge, layered ambient accents,
 * and tactile tap-to-morph spring feedback.
 */
@Composable
fun ExpressiveEmptyState(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    tone: EmptyStateTone = EmptyStateTone.Default,
    contained: Boolean = false,
    action: (@Composable () -> Unit)? = null,
) {
    val motionScheme = MaterialTheme.motionScheme
    val heroScale = remember { Animatable(0.65f) }
    val heroAlpha = remember { Animatable(0f) }
    val heroTilt = remember { Animatable(-10f) }
    val textAlpha = remember { Animatable(0f) }
    val textOffsetY = remember { Animatable(16f) }
    val actionAlpha = remember { Animatable(0f) }
    val actionScale = remember { Animatable(0.9f) }

    LaunchedEffect(Unit) {
        launch { heroAlpha.animateTo(1f, motionScheme.defaultEffectsSpec()) }
        launch { heroScale.animateTo(1f, motionScheme.defaultSpatialSpec()) }
        launch { heroTilt.animateTo(0f, motionScheme.defaultSpatialSpec()) }

        delay(70L.milliseconds)
        launch { textAlpha.animateTo(1f, motionScheme.defaultEffectsSpec()) }
        launch { textOffsetY.animateTo(0f, motionScheme.defaultSpatialSpec()) }

        delay(70L.milliseconds)
        launch { actionAlpha.animateTo(1f, motionScheme.defaultEffectsSpec()) }
        launch { actionScale.animateTo(1f, motionScheme.defaultSpatialSpec()) }
    }

    val colors = toneColors(tone)
    val containerColor = colors.first
    val contentColor = colors.second
    val polygons = remember(tone) { tonePolygons(tone) }

    val body: @Composable () -> Unit = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MorphingShapeBadge(
                icon = icon,
                polygons = polygons,
                containerColor = containerColor,
                contentColor = contentColor,
                modifier = Modifier.graphicsLayer {
                    alpha = heroAlpha.value
                    scaleX = heroScale.value
                    scaleY = heroScale.value
                    rotationZ = heroTilt.value
                },
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .graphicsLayer {
                        alpha = textAlpha.value
                        translationY = textOffsetY.value.dp.toPx()
                    },
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                if (!description.isNullOrBlank()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (action != null) {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .graphicsLayer {
                            alpha = actionAlpha.value
                            scaleX = actionScale.value
                            scaleY = actionScale.value
                        },
                ) {
                    action()
                }
            }
        }
    }

    Box(
        modifier = modifier.padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (contained) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.widthIn(max = 420.dp),
            ) {
                body()
            }
        } else {
            body()
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MorphingShapeBadge(
    icon: ImageVector,
    polygons: List<RoundedPolygon>,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val motionScheme = MaterialTheme.motionScheme
    val scope = rememberCoroutineScope()

    val morphs = remember(polygons) {
        List(polygons.size) { index ->
            Morph(polygons[index], polygons[(index + 1) % polygons.size])
        }
    }
    var activeMorphIndex by remember(polygons) { mutableIntStateOf(0) }
    var tapGeneration by remember { mutableIntStateOf(0) }
    val morphProgress = remember { Animatable(0f) }
    val tapScale = remember { Animatable(1f) }

    // Continuous spring-driven morph loop; restarts cleanly after tap interactions.
    LaunchedEffect(polygons, tapGeneration) {
        if (tapGeneration > 0) {
            morphProgress.animateTo(1f, motionScheme.defaultSpatialSpec())
            activeMorphIndex = (activeMorphIndex + 1) % morphs.size
            morphProgress.snapTo(0f)
        }
        while (true) {
            delay(2200L.milliseconds)
            morphProgress.animateTo(1f, motionScheme.slowSpatialSpec())
            activeMorphIndex = (activeMorphIndex + 1) % morphs.size
            morphProgress.snapTo(0f)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "empty_state_ambient")
    val ambientRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shape_rotation",
    )
    val breathe by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "ambient_breathe",
    )

    // Pre-allocated Path and Matrix reused every frame to avoid GC churn.
    val corePath = remember { Path() }
    val haloPath = remember { Path() }
    val scaleMatrix = remember { Matrix() }

    val haloColor = containerColor.copy(alpha = 0.28f)
    val accentDotColor = contentColor.copy(alpha = 0.35f)
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(136.dp)
            .graphicsLayer {
                scaleX = tapScale.value
                scaleY = tapScale.value
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
            ) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                tapGeneration++
                scope.launch {
                    tapScale.animateTo(0.88f, motionScheme.fastSpatialSpec())
                    tapScale.animateTo(1f, motionScheme.defaultSpatialSpec())
                }
            }
            .drawBehind {
                val morph = morphs[activeMorphIndex]
                val clampedProgress = morphProgress.value.coerceIn(0f, 1f)
                val coreSize = size.minDimension * 0.72f
                val haloSize = coreSize * (1.16f + 0.05f * breathe)

                // Soft breathing outer halo matching the morphing silhouette
                haloPath.rewind()
                morph.toPath(progress = clampedProgress, path = haloPath, startAngle = 0)
                scaleMatrix.reset()
                scaleMatrix.scale(x = haloSize, y = haloSize)
                haloPath.transform(scaleMatrix)
                haloPath.translate(size.center - haloPath.getBounds().center)
                rotate(degrees = -ambientRotation * 0.5f, pivot = size.center) {
                    drawPath(path = haloPath, color = haloColor)
                }

                // Subtle orbiting accent dots
                val orbitRadius = size.minDimension * 0.45f
                val baseAngleRad = (ambientRotation * 1.4f) * (PI / 180.0)
                val dotRadii = floatArrayOf(4.5.dp.toPx(), 3.dp.toPx(), 3.5.dp.toPx())
                for (i in dotRadii.indices) {
                    val angle = baseAngleRad + i * (2.0 * PI / dotRadii.size)
                    val cx = size.center.x + orbitRadius * cos(angle).toFloat()
                    val cy = size.center.y + orbitRadius * sin(angle).toFloat()
                    drawCircle(
                        color = accentDotColor,
                        radius = dotRadii[i],
                        center = Offset(cx, cy),
                    )
                }

                // Main morphing MaterialShapes polygon
                corePath.rewind()
                morph.toPath(progress = clampedProgress, path = corePath, startAngle = 0)
                scaleMatrix.reset()
                scaleMatrix.scale(x = coreSize, y = coreSize)
                corePath.transform(scaleMatrix)
                corePath.translate(size.center - corePath.getBounds().center)
                rotate(degrees = ambientRotation, pivot = size.center) {
                    drawPath(path = corePath, color = containerColor)
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier
                .size(40.dp)
                .graphicsLayer {
                    val iconScale = 0.96f + 0.06f * breathe
                    scaleX = iconScale
                    scaleY = iconScale
                },
        )
    }
}

@Composable
private fun toneColors(tone: EmptyStateTone): Pair<Color, Color> = when (tone) {
    EmptyStateTone.Default ->
        MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer

    EmptyStateTone.Celebratory ->
        MaterialTheme.semanticColors.successContainer to MaterialTheme.semanticColors.onSuccessContainer

    EmptyStateTone.Search ->
        MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer

    EmptyStateTone.Notice ->
        MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun tonePolygons(tone: EmptyStateTone): List<RoundedPolygon> = when (tone) {
    EmptyStateTone.Default -> listOf(
        MaterialShapes.Cookie6Sided,
        MaterialShapes.Clover4Leaf,
        MaterialShapes.SoftBoom,
        MaterialShapes.Flower,
    )

    EmptyStateTone.Celebratory -> listOf(
        MaterialShapes.Sunny,
        MaterialShapes.Cookie9Sided,
        MaterialShapes.Clover4Leaf,
        MaterialShapes.SoftBurst,
    )

    EmptyStateTone.Search -> listOf(
        MaterialShapes.Slanted,
        MaterialShapes.Ghostish,
        MaterialShapes.Oval,
        MaterialShapes.PuffyDiamond,
    )

    EmptyStateTone.Notice -> listOf(
        MaterialShapes.Cookie4Sided,
        MaterialShapes.Clover8Leaf,
        MaterialShapes.Puffy,
        MaterialShapes.Arch,
    )
}

@Preview
@Composable
private fun ExpressiveEmptyStateDefaultPreview() {
    MerrPatentenTheme {
        Surface {
            ExpressiveEmptyState(
                icon = Star,
                title = "No Saved Questions",
                description = "Tap the star icon on any exam question to save it here for quick review.",
                tone = EmptyStateTone.Default,
            )
        }
    }
}

@Preview
@Composable
private fun ExpressiveEmptyStateCelebratoryPreview() {
    MerrPatentenTheme {
        Surface {
            ExpressiveEmptyState(
                icon = CircleCheck,
                title = "No Mistakes Recorded!",
                description = "Complete exam simulations to discover and track questions that need extra practice.",
                tone = EmptyStateTone.Celebratory,
            )
        }
    }
}

@Preview
@Composable
private fun ExpressiveEmptyStateNoticeDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        Surface {
            ExpressiveEmptyState(
                icon = CloudExclamationPoint,
                title = "Result saving is disabled",
                tone = EmptyStateTone.Notice,
                action = {
                    FilledTonalButton(
                        onClick = {},
                        shapes = ButtonDefaults.shapes(),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                    ) {
                        Text(text = "Change setting")
                    }
                },
            )
        }
    }
}

@Preview
@Composable
private fun ExpressiveEmptyStateSearchPreview() {
    MerrPatentenTheme {
        Surface {
            ExpressiveEmptyState(
                icon = SearchX,
                title = "No signs or rules found",
                tone = EmptyStateTone.Search,
            )
        }
    }
}
