package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import com.arsildo.merrpatenten.shared.core.designsystem.icons.ArrowUp
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleX
import com.arsildo.merrpatenten.shared.core.designsystem.icons.LayoutGrid
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Timer
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExamLegend(
    pagerState: PagerState,
    timer: () -> String,
    endExamVisible: Boolean,
    onMapClick: () -> Unit,
    onShowEndExamClick: () -> Unit,
    modifier: Modifier = Modifier,
    showMapButton: Boolean = true,
) {
    val hapticFeedback = LocalHapticFeedback.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Question Counter Pill
        QuestionCounterPill(pagerState = pagerState)

        // Map / Grid Action
        if (showMapButton) {
            FilledTonalButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onMapClick()
                },
                shapes = ButtonShapes(
                    shape = MaterialTheme.shapes.medium,
                    pressedShape = MaterialTheme.shapes.small,
                ),
                modifier = Modifier.padding(horizontal = 4.dp),
            ) {
                Icon(
                    imageVector = LayoutGrid,
                    contentDescription = stringResource(Res.string.question_map),
                    modifier = Modifier.size(18.dp),
                )
            }
        }

        // Timer Badge
        ExamTimerBadge(timer = timer)

        val rotationState = animateFloatAsState(
            targetValue = if (endExamVisible) 180f else 0f,
            label = "EndExamRotation",
            animationSpec = tween(durationMillis = 350),
        )

        val actionColor by animateColorAsState(
            targetValue = if (endExamVisible) MaterialTheme.colorScheme.primary else Red,
            label = "EndExamColor",
        )

        FilledTonalIconButton(
            onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onShowEndExamClick()
            },
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = actionColor.copy(alpha = 0.12f),
                contentColor = actionColor,
            ),
            shape = CircleShape,
            modifier = Modifier.size(40.dp),
        ) {
            Icon(
                imageVector = if (endExamVisible) ArrowUp else CircleX,
                contentDescription = stringResource(Res.string.toggle_end_exam),
                modifier = Modifier
                    .size(22.dp)
                    .graphicsLayer { rotationZ = rotationState.value },
            )
        }
    }
}

@Composable
private fun QuestionCounterPill(pagerState: PagerState, modifier: Modifier = Modifier) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = modifier,
    ) {
        Text(
            text = "${pagerState.currentPage + 1}/${pagerState.pageCount}",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun ExamTimerBadge(timer: () -> String, modifier: Modifier = Modifier) {
    val timerText = timer()
    val isLowTime =
        timerText.startsWith("0") && (timerText.startsWith("00:") || timerText.startsWith("01:") || timerText.startsWith("02:"))
    Surface(
        shape = CircleShape,
        color = if (isLowTime) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.tertiaryContainer
        },
        contentColor = if (isLowTime) {
            MaterialTheme.colorScheme.onErrorContainer
        } else {
            MaterialTheme.colorScheme.onTertiaryContainer
        },
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Timer,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = timerText,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
private fun ExamLegendPreview() {
    MerrPatentenTheme {
        ExamLegend(
            pagerState = rememberPagerState(initialPage = 0, pageCount = { QUESTIONS_IN_EXAM }),
            timer = { "39:42" },
            endExamVisible = false,
            onMapClick = {},
            onShowEndExamClick = {},
        )
    }
}

@Preview
@Composable
private fun ExamLegendLowTimePreview() {
    MerrPatentenTheme {
        ExamLegend(
            pagerState = rememberPagerState(initialPage = 38, pageCount = { QUESTIONS_IN_EXAM }),
            timer = { "01:15" },
            endExamVisible = true,
            onMapClick = {},
            onShowEndExamClick = {},
        )
    }
}

@Preview
@Composable
private fun ExamLegendDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        ExamLegend(
            pagerState = rememberPagerState(initialPage = 15, pageCount = { QUESTIONS_IN_EXAM }),
            timer = { "25:30" },
            endExamVisible = false,
            onMapClick = {},
            onShowEndExamClick = {},
        )
    }
}
