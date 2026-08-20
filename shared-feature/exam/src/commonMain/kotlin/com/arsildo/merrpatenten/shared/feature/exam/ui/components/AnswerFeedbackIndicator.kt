package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AnswerFeedbackIndicator(
    correctAnswer: Boolean,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (correctAnswer) MaterialTheme.semanticColors.successContainer else MaterialTheme.colorScheme.errorContainer
    val contentColor = if (correctAnswer) MaterialTheme.semanticColors.onSuccessContainer else MaterialTheme.colorScheme.onErrorContainer
    val iconColor = if (correctAnswer) MaterialTheme.semanticColors.success else MaterialTheme.colorScheme.error

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (correctAnswer) Icons.Rounded.Check else Icons.Rounded.Clear,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = stringResource(if (correctAnswer) Res.string.correct_answer_feedback else Res.string.wrong_answer_feedback),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun AnswerFeedbackIndicatorCorrectPreview() {
    MerrPatentenTheme {
        AnswerFeedbackIndicator(
            correctAnswer = true,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun AnswerFeedbackIndicatorWrongPreview() {
    MerrPatentenTheme {
        AnswerFeedbackIndicator(
            correctAnswer = false,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun AnswerFeedbackIndicatorDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        AnswerFeedbackIndicator(
            correctAnswer = true,
            modifier = Modifier.padding(16.dp)
        )
    }
}
