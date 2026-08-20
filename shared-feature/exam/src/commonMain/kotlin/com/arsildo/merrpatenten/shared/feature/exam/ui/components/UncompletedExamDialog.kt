package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun UncompletedExamDialog(
    onDismissRequest: () -> Unit,
    onReviewUnanswered: () -> Unit,
    onFinishAnyway: () -> Unit,
    onExitExam: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = MaterialTheme.shapes.extraLarge,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        icon = {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Rounded.DoneAll,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        title = {
            Text(
                text = stringResource(Res.string.uncompleted_dialog_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.uncompleted_dialog_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onReviewUnanswered()
                    },
                    shapes = ButtonShapes(
                        shape = MaterialTheme.shapes.medium,
                        pressedShape = MaterialTheme.shapes.small
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(14.dp),
                    content = {
                        Text(
                            text = stringResource(Res.string.review_unanswered),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )
                FilledTonalButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                        onFinishAnyway()
                    },
                    shapes = ButtonShapes(
                        shape = MaterialTheme.shapes.medium,
                        pressedShape = MaterialTheme.shapes.small
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(14.dp),
                    content = {
                        Text(
                            text = stringResource(Res.string.finish_anyway),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )
                Button(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onExitExam()
                    },
                    shapes = ButtonShapes(
                        shape = MaterialTheme.shapes.medium,
                        pressedShape = MaterialTheme.shapes.small
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    content = {
                        Text(
                            text = stringResource(Res.string.exit_exam_button),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun UncompletedExamDialogPreview() {
    MerrPatentenTheme {
        UncompletedExamDialog(
            onDismissRequest = {},
            onReviewUnanswered = {},
            onFinishAnyway = {},
            onExitExam = {}
        )
    }
}

@Preview
@Composable
private fun UncompletedExamDialogDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        UncompletedExamDialog(
            onDismissRequest = {},
            onReviewUnanswered = {},
            onFinishAnyway = {},
            onExitExam = {}
        )
    }
}
