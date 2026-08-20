package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.QuestionTextSize
import com.arsildo.merrpatenten.shared.core.designsystem.formatQuestion
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun QuestionItem(
    isCompleted: Boolean,
    correct: Boolean,
    image: DrawableResource,
    onImageClick: () -> Unit,
    question: String,
    falseChecked: Boolean,
    trueChecked: Boolean,
    questionTextSize: QuestionTextSize = QuestionTextSize.DEFAULT,
    onFalseCheckedChange: (Boolean) -> Unit,
    onTrueCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Question Image
                QuestionImage(image = image, onClick = onImageClick)

                Spacer(modifier = Modifier.height(16.dp))

                // Question Text
                Text(
                    text = formatQuestion(question),
                    textAlign = TextAlign.Center,
                    style = questionTextSize.textStyle,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expressive Option Cards & Feedback
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ExpressiveOptionCard(
                        title = stringResource(Res.string.false_checkbox),
                        iconSelected = Icons.Rounded.Cancel,
                        iconUnselected = Icons.Outlined.Cancel,
                        isChecked = falseChecked,
                        enabled = !isCompleted,
                        selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                        selectedContentColor = MaterialTheme.colorScheme.onErrorContainer,
                        selectedBorderColor = MaterialTheme.colorScheme.error,
                        onClick = { onFalseCheckedChange(!falseChecked) },
                        modifier = Modifier.weight(1f),
                    )

                    ExpressiveOptionCard(
                        title = stringResource(Res.string.true_checkbox),
                        iconSelected = Icons.Rounded.CheckCircle,
                        iconUnselected = Icons.Outlined.CheckCircle,
                        isChecked = trueChecked,
                        enabled = !isCompleted,
                        selectedContainerColor = MaterialTheme.semanticColors.successContainer,
                        selectedContentColor = MaterialTheme.semanticColors.onSuccessContainer,
                        selectedBorderColor = MaterialTheme.semanticColors.success,
                        onClick = { onTrueCheckedChange(!trueChecked) },
                        modifier = Modifier.weight(1f),
                    )
                }

                if (isCompleted) {
                    AnswerFeedbackIndicator(correctAnswer = correct)
                }
            }
        }
    }
}

@Preview
@Composable
private fun QuestionItemInProgressPreview() {
    MerrPatentenTheme {
        QuestionItem(
            isCompleted = false,
            correct = false,
            image = getImageResource(1),
            onImageClick = {},
            question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
            falseChecked = false,
            trueChecked = true,
            onFalseCheckedChange = {},
            onTrueCheckedChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun QuestionItemCompletedCorrectPreview() {
    MerrPatentenTheme {
        QuestionItem(
            isCompleted = true,
            correct = true,
            image = getImageResource(1),
            onImageClick = {},
            question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
            falseChecked = false,
            trueChecked = true,
            onFalseCheckedChange = {},
            onTrueCheckedChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun QuestionItemCompletedWrongPreview() {
    MerrPatentenTheme {
        QuestionItem(
            isCompleted = true,
            correct = false,
            image = getImageResource(1),
            onImageClick = {},
            question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
            falseChecked = true,
            trueChecked = false,
            onFalseCheckedChange = {},
            onTrueCheckedChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun QuestionItemDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        QuestionItem(
            isCompleted = false,
            correct = false,
            image = getImageResource(1),
            onImageClick = {},
            question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
            falseChecked = false,
            trueChecked = false,
            onFalseCheckedChange = {},
            onTrueCheckedChange = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
