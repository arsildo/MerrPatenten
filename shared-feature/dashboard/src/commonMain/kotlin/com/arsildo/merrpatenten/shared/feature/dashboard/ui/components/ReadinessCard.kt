package com.arsildo.merrpatenten.shared.feature.dashboard.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.data.ReadinessLevel
import com.arsildo.merrpatenten.shared.core.data.ReadinessScore
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReadinessCard(
    readinessScore: ReadinessScore,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ),
) {
    val hapticFeedback = LocalHapticFeedback.current

    val (badgeColor, onBadgeColor, levelText, icon) = when (readinessScore.level) {
        ReadinessLevel.READY -> Quadruple(
            MaterialTheme.semanticColors.successContainer,
            MaterialTheme.semanticColors.onSuccessContainer,
            stringResource(Res.string.readiness_status_ready),
            Icons.Rounded.Verified,
        )

        ReadinessLevel.IN_PROGRESS -> Quadruple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            stringResource(Res.string.readiness_status_in_progress),
            Icons.AutoMirrored.Rounded.TrendingUp,
        )

        ReadinessLevel.NEEDS_PRACTICE -> Quadruple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            stringResource(Res.string.readiness_status_needs_practice),
            Icons.Rounded.WarningAmber,
        )

        ReadinessLevel.NOT_ENOUGH_DATA -> Quadruple(
            MaterialTheme.colorScheme.surfaceContainerHighest,
            MaterialTheme.colorScheme.onSurfaceVariant,
            stringResource(Res.string.readiness_status_not_enough_data),
            Icons.Rounded.Insights,
        )
    }

    val animatedProgress by animateFloatAsState(
        targetValue = (readinessScore.percentage / 100f).coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label = "readiness_progress",
    )

    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = colors,
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            onClick()
        },
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Header Row: Title + Arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(badgeColor),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = onBadgeColor,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(Res.string.readiness_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = levelText,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = onBadgeColor,
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            // Progress & Percentage Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(MaterialTheme.shapes.small),
                    color = if (readinessScore.level == ReadinessLevel.READY) {
                        MaterialTheme.semanticColors.success
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                )
                Text(
                    text = "${readinessScore.percentage}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            // Subtitle Description
            val subtitle = if (readinessScore.level == ReadinessLevel.NOT_ENOUGH_DATA && readinessScore.completedExams < 3) {
                stringResource(Res.string.readiness_initial_hint)
            } else {
                stringResource(
                    Res.string.readiness_subtitle_format,
                    readinessScore.completedExams,
                    readinessScore.passRate,
                )
            }

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Preview
@Composable
private fun ReadinessCardReadyPreview() {
    MerrPatentenTheme {
        ReadinessCard(
            readinessScore = ReadinessScore(
                percentage = 88,
                level = ReadinessLevel.READY,
                completedExams = 10,
                passedExams = 9,
                passRate = 90,
                averageErrors = 1.8,
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun ReadinessCardInProgressPreview() {
    MerrPatentenTheme {
        ReadinessCard(
            readinessScore = ReadinessScore(
                percentage = 65,
                level = ReadinessLevel.IN_PROGRESS,
                completedExams = 6,
                passedExams = 4,
                passRate = 67,
                averageErrors = 3.5,
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun ReadinessCardEmptyPreview() {
    MerrPatentenTheme {
        ReadinessCard(
            readinessScore = ReadinessScore.EMPTY,
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
