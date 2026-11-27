package com.arsildo.merrpatenten.shared.feature.exam.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CheckCheck
import com.arsildo.merrpatenten.shared.feature.exam.ExamViewModel
import com.arsildo.merrpatenten.shared.feature.exam.navigation.ExamType
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
internal fun ExitExamBottomSheetRoute(
    modifier: Modifier = Modifier,
    category: String,
    type: ExamType,
    viewModel: ExamViewModel = koinViewModel(parameters = { parametersOf(category, type) }),
    onReviewUnanswered: () -> Unit,
    onFinishAnyway: () -> Unit,
    onExitExam: () -> Unit,
    onDismiss: () -> Unit,
) {
    ExitExamBottomSheet(
        onReviewUnanswered = onReviewUnanswered,
        onFinishAnyway = {
            viewModel.completeExam()
            onFinishAnyway()
        },
        onExitExam = onExitExam,
        onDismiss = onDismiss,
        modifier = modifier,
    )
}

@Composable
internal fun ExitExamBottomSheet(
    modifier: Modifier = Modifier,
    onReviewUnanswered: () -> Unit,
    onFinishAnyway: () -> Unit,
    onExitExam: () -> Unit,
    onDismiss: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.Escape, Key.Back -> {
                        onDismiss()
                        true
                    }

                    else -> false
                }
            }
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.size(64.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = CheckCheck,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(32.dp),
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(Res.string.uncompleted_dialog_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(Res.string.uncompleted_dialog_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onReviewUnanswered()
                },
                shapes = ButtonShapes(
                    shape = MaterialTheme.shapes.medium,
                    pressedShape = MaterialTheme.shapes.small,
                ),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.review_unanswered),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            FilledTonalButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                    onFinishAnyway()
                },
                shapes = ButtonShapes(
                    shape = MaterialTheme.shapes.medium,
                    pressedShape = MaterialTheme.shapes.small,
                ),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.finish_anyway),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Button(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onExitExam()
                },
                shapes = ButtonShapes(
                    shape = MaterialTheme.shapes.medium,
                    pressedShape = MaterialTheme.shapes.small,
                ),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
            ) {
                Text(
                    text = stringResource(Res.string.exit_exam_button),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        Spacer(
            modifier = Modifier
                .size(16.dp)
                .navigationBarsPadding(),
        )
    }
}

@Preview
@Composable
private fun ExitExamBottomSheetPreview() = MerrPatentenTheme {
    Surface {
        ExitExamBottomSheet(
            onReviewUnanswered = {},
            onFinishAnyway = {},
            onExitExam = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun ExitExamBottomSheetDarkPreview() = MerrPatentenTheme(darkTheme = true) {
    Surface {
        ExitExamBottomSheet(
            onReviewUnanswered = {},
            onFinishAnyway = {},
            onExitExam = {},
            onDismiss = {},
        )
    }
}
