package com.arsildo.merrpatenten.shared.feature.exam

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Map(
    isCompleted: Boolean,
    errors: Int,
    mistakes: List<Int>,
    responses: List<String>,
    onQuestionClicked: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = {
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .size(width = 36.dp, height = 4.dp)
            ) {}
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier
    ) {
        MapContent(
            isCompleted = isCompleted,
            errors = errors,
            mistakes = mistakes,
            responses = responses,
            onQuestionClicked = onQuestionClicked,
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
fun MapContent(
    isCompleted: Boolean,
    errors: Int,
    mistakes: List<Int>,
    responses: List<String>,
    onQuestionClicked: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isPassed = errors <= ERRORS_ALLOWED
    val heroContainer = if (isPassed) MaterialTheme.semanticColors.successContainer else MaterialTheme.colorScheme.errorContainer
    val heroContent = if (isPassed) MaterialTheme.semanticColors.onSuccessContainer else MaterialTheme.colorScheme.onErrorContainer
    val heroIcon = if (isPassed) Icons.Rounded.CheckCircle else Icons.Rounded.HighlightOff
    val heroIconColor = if (isPassed) MaterialTheme.semanticColors.success else MaterialTheme.colorScheme.error

    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            if (isPassed) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
            } else {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.Reject)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCompleted) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = heroContainer,
                contentColor = heroContent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = heroIcon,
                            contentDescription = null,
                            tint = heroIconColor,
                            modifier = Modifier.size(32.dp)
                        )
                        Column {
                            Text(
                                text = stringResource(if (isPassed) Res.string.passed else Res.string.failed),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(if (isPassed) Res.string.exam_passed_subtitle else Res.string.exam_failed_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = heroContent.copy(alpha = 0.8f)
                            )
                        }
                    }

                    val errorLabel = stringResource(if (errors == 1) Res.string.error_singular else Res.string.errors_plural)
                    Surface(
                        shape = CircleShape,
                        color = heroIconColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "$errors $errorLabel",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = heroIconColor,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(count = QUESTIONS_IN_EXAM) { page ->
                val hasResponse = page < responses.size && responses[page].isNotEmpty()
                val isCorrect = page < mistakes.size && mistakes[page] == 0

                val container = if (isCompleted) {
                    if (isCorrect) MaterialTheme.semanticColors.successContainer else MaterialTheme.colorScheme.errorContainer
                } else if (hasResponse) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHigh
                }

                val content = if (isCompleted) {
                    if (isCorrect) MaterialTheme.semanticColors.onSuccessContainer else MaterialTheme.colorScheme.onErrorContainer
                } else if (hasResponse) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }

                val isSelected = hasResponse

                val statusText = if (isCompleted) {
                    stringResource(if (isCorrect) Res.string.true_checkbox else Res.string.false_checkbox)
                } else if (hasResponse) {
                    stringResource(Res.string.completed_question)
                } else {
                    stringResource(Res.string.uncompleted_question)
                }
                val questionTitle = stringResource(Res.string.question_number_format, page + 1)

                QuestionGridItem(
                    title = page,
                    containerColor = container,
                    contentColor = content,
                    shape = MaterialTheme.shapes.medium,
                    contentDescription = stringResource(Res.string.question_status_format, questionTitle, statusText),
                    onClick = { onQuestionClicked(page) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isCompleted) {
                StatusIndicator(
                    title = stringResource(Res.string.completed_question),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusIndicator(
                        title = stringResource(Res.string.false_checkbox),
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    )
                    StatusIndicator(
                        title = stringResource(Res.string.true_checkbox),
                        containerColor = MaterialTheme.semanticColors.successContainer,
                        contentColor = MaterialTheme.semanticColors.onSuccessContainer,
                    )
                }
            }
            IconButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onDismissRequest()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = stringResource(Res.string.close),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun QuestionGridItem(
    title: Int,
    containerColor: Color,
    contentColor: Color,
    contentDescription: String,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    Surface(
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentTick)
            onClick()
        },
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        modifier = Modifier
            .aspectRatio(1f)
            .semantics { this.contentDescription = contentDescription }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "${title + 1}",
                color = contentColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StatusIndicator(
    title: String,
    containerColor: Color,
    contentColor: Color,
) {
    Surface(
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(contentColor)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MapInProgressPreview() {
    MerrPatentenTheme {
        Map(
            sheetState = rememberModalBottomSheetState(),
            isCompleted = false,
            responses = List(QUESTIONS_IN_EXAM) { if (it < 15) "Saktë" else "" },
            mistakes = List(QUESTIONS_IN_EXAM) { 0 },
            errors = 0,
            onQuestionClicked = {},
            onDismissRequest = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MapPassedPreview() {
    MerrPatentenTheme {
        Map(
            sheetState = rememberModalBottomSheetState(),
            isCompleted = true,
            responses = List(QUESTIONS_IN_EXAM) { "Saktë" },
            mistakes = List(QUESTIONS_IN_EXAM) { if (it < 2) 1 else 0 },
            errors = 2,
            onQuestionClicked = {},
            onDismissRequest = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MapFailedPreview() {
    MerrPatentenTheme {
        Map(
            sheetState = rememberModalBottomSheetState(),
            isCompleted = true,
            responses = List(QUESTIONS_IN_EXAM) { "Saktë" },
            mistakes = List(QUESTIONS_IN_EXAM) { if (it < 7) 1 else 0 },
            errors = 7,
            onQuestionClicked = {},
            onDismissRequest = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MapDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        Map(
            sheetState = rememberModalBottomSheetState(),
            isCompleted = false,
            responses = List(QUESTIONS_IN_EXAM) { if (it < 10) "Saktë" else "" },
            mistakes = List(QUESTIONS_IN_EXAM) { 0 },
            errors = 0,
            onQuestionClicked = {},
            onDismissRequest = {}
        )
    }
}
