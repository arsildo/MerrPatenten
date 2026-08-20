package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.rounded.ArrowCircleUp
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ExamLegend(
    pagerState: PagerState,
    timer: () -> String,
    endExamVisible: Boolean,
    onMapClick: () -> Unit,
    onShowEndExamButton: () -> Unit,
    modifier: Modifier = Modifier,
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
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            Text(
                text = "${pagerState.currentPage + 1}/$QUESTIONS_IN_EXAM",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            )
        }

        // Map / Grid Action
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
                imageVector = Icons.Rounded.GridView,
                contentDescription = stringResource(Res.string.question_map),
                modifier = Modifier.size(18.dp),
            )
        }

        // Timer Badge
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
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Timer,
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

        val rotationState by animateFloatAsState(
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
                onShowEndExamButton()
            },
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = actionColor.copy(alpha = 0.12f),
                contentColor = actionColor,
            ),
            shape = CircleShape,
            modifier = Modifier.size(40.dp),
        ) {
            Icon(
                imageVector = if (endExamVisible) Icons.Rounded.ArrowCircleUp else Icons.Outlined.Cancel,
                contentDescription = stringResource(Res.string.toggle_end_exam),
                modifier = Modifier
                    .size(22.dp)
                    .rotate(rotationState),
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
            onShowEndExamButton = {},
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
            onShowEndExamButton = {},
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
            onShowEndExamButton = {},
        )
    }
}
