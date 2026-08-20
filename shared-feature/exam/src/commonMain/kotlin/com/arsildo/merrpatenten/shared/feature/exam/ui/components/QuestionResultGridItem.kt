package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors

@Composable
internal fun QuestionResultGridItem(
    title: Int,
    containerColor: Color,
    contentColor: Color,
    contentDescription: String,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
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
        modifier = modifier
            .aspectRatio(1f)
            .semantics { this.contentDescription = contentDescription },
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "${title + 1}",
                color = contentColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview
@Composable
private fun QuestionResultGridItemCorrectPreview() {
    MerrPatentenTheme {
        QuestionResultGridItem(
            title = 0,
            containerColor = MaterialTheme.semanticColors.successContainer,
            contentColor = MaterialTheme.semanticColors.onSuccessContainer,
            contentDescription = "Pyetja 1, Saktë",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun QuestionResultGridItemWrongPreview() {
    MerrPatentenTheme {
        QuestionResultGridItem(
            title = 1,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            contentDescription = "Pyetja 2, Gabuar",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun QuestionResultGridItemUnansweredPreview() {
    MerrPatentenTheme {
        QuestionResultGridItem(
            title = 2,
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = "Pyetja 3, Papërfunduar",
            onClick = {},
        )
    }
}
