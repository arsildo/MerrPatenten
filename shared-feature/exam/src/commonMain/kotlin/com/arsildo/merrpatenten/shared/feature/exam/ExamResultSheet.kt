package com.arsildo.merrpatenten.shared.feature.exam

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.designsystem.Green
import com.arsildo.merrpatenten.shared.core.designsystem.GreenContainer
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.OnGreenContainer
import com.arsildo.merrpatenten.shared.core.designsystem.OnRedContainer
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import com.arsildo.merrpatenten.shared.core.designsystem.RedContainer
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExitExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.RestartExamButton
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExamResultBottomSheetRoute(
    onQuestionClicked: (Int) -> Unit,
    onExitExam: () -> Unit,
    onRestartExam: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: ExamViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExamResultBottomSheet(
        uiState = uiState,
        onQuestionClicked = { page ->
            viewModel.navigateToPage(page)
            onQuestionClicked(page)
        },
        onExitExam = onExitExam,
        onRestartExam = onRestartExam,
        onDismiss = onDismiss,
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExamResultBottomSheet(
    uiState: ExamUiState,
    onQuestionClicked: (Int) -> Unit,
    onExitExam: () -> Unit,
    onRestartExam: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isPassed = uiState.errors <= ERRORS_ALLOWED
    val heroContainer = if (isPassed) GreenContainer else RedContainer
    val heroContent = if (isPassed) OnGreenContainer else OnRedContainer
    val heroIcon = if (isPassed) Icons.Rounded.CheckCircle else Icons.Rounded.HighlightOff
    val heroIconColor = if (isPassed) Green else Red

    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier
                .padding(top = 12.dp)
                .size(width = 36.dp, height = 4.dp)
        ) {}

        if (uiState.isCompleted) {
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
                        .padding(16.dp),
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

                    val errorLabel = stringResource(if (uiState.errors == 1) Res.string.error_singular else Res.string.errors_plural)
                    Surface(
                        shape = CircleShape,
                        color = heroIconColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${uiState.errors} $errorLabel",
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
                val hasResponse = page < uiState.responseList.size && uiState.responseList[page].isNotEmpty()
                val isCorrect = page < uiState.mistakePositions.size && uiState.mistakePositions[page] == 0

                val container = if (uiState.isCompleted) {
                    if (isCorrect) GreenContainer else RedContainer
                } else if (hasResponse) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHigh
                }

                val content = if (uiState.isCompleted) {
                    if (isCorrect) OnGreenContainer else OnRedContainer
                } else if (hasResponse) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }

                val shape = if (uiState.isCompleted) {
                    if (isCorrect) MaterialShapes.Sunny.toShape() else MaterialShapes.Cookie9Sided.toShape()
                } else if (hasResponse) {
                    MaterialShapes.Slanted.toShape()
                } else {
                    MaterialShapes.Cookie9Sided.toShape()
                }

                QuestionResultGridItem(
                    title = page,
                    containerColor = container,
                    contentColor = content,
                    shape = shape,
                    onClick = { onQuestionClicked(page) }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!uiState.isCompleted) {
                ResultStatusIndicator(
                    title = stringResource(Res.string.completed_question),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ResultStatusIndicator(
                        title = stringResource(Res.string.false_checkbox),
                        containerColor = RedContainer,
                        contentColor = OnRedContainer,
                    )
                    ResultStatusIndicator(
                        title = stringResource(Res.string.true_checkbox),
                        containerColor = GreenContainer,
                        contentColor = OnGreenContainer,
                    )
                }
            }
            IconButton(
                onClick = onDismiss,
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

        if (uiState.isCompleted) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ExitExamButton(
                    icon = Icons.AutoMirrored.Rounded.ExitToApp,
                    onClick = onExitExam,
                    modifier = Modifier.weight(1f)
                )
                RestartExamButton(
                    icon = Icons.Rounded.RestartAlt,
                    onClick = onRestartExam,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun QuestionResultGridItem(
    title: Int,
    containerColor: Color,
    contentColor: Color,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(shape)
            .aspectRatio(1f)
            .background(containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${title + 1}",
            color = contentColor,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ResultStatusIndicator(
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

@Preview
@Composable
private fun ExamResultBottomSheetInProgressPreview() {
    MerrPatentenTheme {
        ExamResultBottomSheet(
            uiState = ExamUiState(
                isCompleted = false,
                responseList = List(QUESTIONS_IN_EXAM) { if (it < 10) "Saktë" else "" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { 1 }
            ),
            onQuestionClicked = {},
            onExitExam = {},
            onRestartExam = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun ExamResultBottomSheetPassedPreview() {
    MerrPatentenTheme {
        ExamResultBottomSheet(
            uiState = ExamUiState(
                isCompleted = true,
                errors = 2,
                responseList = List(QUESTIONS_IN_EXAM) { "Saktë" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { if (it < 2) 1 else 0 }
            ),
            onQuestionClicked = {},
            onExitExam = {},
            onRestartExam = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun ExamResultBottomSheetFailedPreview() {
    MerrPatentenTheme {
        ExamResultBottomSheet(
            uiState = ExamUiState(
                isCompleted = true,
                errors = 8,
                responseList = List(QUESTIONS_IN_EXAM) { "Saktë" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { if (it < 8) 1 else 0 }
            ),
            onQuestionClicked = {},
            onExitExam = {},
            onRestartExam = {},
            onDismiss = {}
        )
    }
}

@Preview
@Composable
private fun ExamResultBottomSheetDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        ExamResultBottomSheet(
            uiState = ExamUiState(
                isCompleted = true,
                errors = 2,
                responseList = List(QUESTIONS_IN_EXAM) { "Saktë" },
                mistakePositions = List(QUESTIONS_IN_EXAM) { if (it < 2) 1 else 0 }
            ),
            onQuestionClicked = {},
            onExitExam = {},
            onRestartExam = {},
            onDismiss = {}
        )
    }
}
