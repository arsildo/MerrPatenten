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
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.designsystem.Green
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.QUESTIONS_IN_EXAM
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExitExamButton
import com.arsildo.merrpatenten.shared.core.designsystem.components.RestartExamButton
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.completed_question
import merrpatenten.shared_core.design_system.generated.resources.failed
import merrpatenten.shared_core.design_system.generated.resources.false_checkbox
import merrpatenten.shared_core.design_system.generated.resources.passed
import merrpatenten.shared_core.design_system.generated.resources.true_checkbox
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

@Composable
fun ExamResultBottomSheet(
    uiState: ExamUiState,
    onQuestionClicked: (Int) -> Unit,
    onExitExam: () -> Unit,
    onRestartExam: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isCompleted) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (uiState.errors > ERRORS_ALLOWED) Red else Green
                ),
                elevation = CardDefaults.elevatedCardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(if (uiState.errors > ERRORS_ALLOWED) Res.string.failed else Res.string.passed),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${uiState.errors} Gabime",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(count = QUESTIONS_IN_EXAM) { page ->
                val hasResponse = page < uiState.responseList.size && uiState.responseList[page].isNotEmpty()
                val isCorrect = page < uiState.mistakePositions.size && uiState.mistakePositions[page] == 0
                QuestionResultGridItem(
                    title = page,
                    containerColor = if (uiState.isCompleted) {
                        if (isCorrect) Green else Red
                    } else if (hasResponse) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    },
                    contentColor = if (uiState.isCompleted) {
                        Color.White
                    } else if (hasResponse) {
                        MaterialTheme.colorScheme.onSecondary
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    },
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
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                )
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ResultStatusIndicator(
                        title = stringResource(Res.string.false_checkbox),
                        containerColor = Red.copy(0.15f),
                        contentColor = Red,
                    )
                    ResultStatusIndicator(
                        title = stringResource(Res.string.true_checkbox),
                        containerColor = Green.copy(0.15f),
                        contentColor = Green,
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
                    contentDescription = null
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
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .aspectRatio(1f)
            .background(containerColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${title + 1}",
            color = contentColor,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ResultStatusIndicator(
    title: String,
    containerColor: Color,
    contentColor: Color,
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(contentColor)
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

