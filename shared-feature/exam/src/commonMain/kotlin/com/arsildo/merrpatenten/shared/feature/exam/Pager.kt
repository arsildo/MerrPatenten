package com.arsildo.merrpatenten.shared.feature.exam

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.QuestionTextSize
import com.arsildo.merrpatenten.shared.core.designsystem.formatQuestion
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.core.model.Question
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Pager(
    questions: List<Question>,
    pagerState: PagerState,
    falseCheckedPages: List<Boolean>,
    trueCheckedPages: List<Boolean>,
    onImageClick: (Int) -> Unit,
    onCheckFalseAtPage: (Int) -> Unit,
    onCheckTrueAtPage: (Int) -> Unit,
    isCompleted: Boolean,
    responses: List<Int>,
    questionTextSize: QuestionTextSize = QuestionTextSize.DEFAULT,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        pageSpacing = 16.dp,
        key = { index -> if (index < questions.size) questions[index].id else index },
        modifier = modifier.fillMaxSize()
    ) { page ->
        if (page < questions.size) {
            QuestionItem(
                isCompleted = isCompleted,
                correct = page < responses.size && responses[page] == 0,
                image = getImageResource(id = questions[page].image),
                onImageClick = { onImageClick(questions[page].image) },
                question = questions[page].question,
                falseChecked = page < falseCheckedPages.size && falseCheckedPages[page],
                trueChecked = page < trueCheckedPages.size && trueCheckedPages[page],
                questionTextSize = questionTextSize,
                onFalseCheckedChange = { onCheckFalseAtPage(page) },
                onTrueCheckedChange = { onCheckTrueAtPage(page) }
            )
        }
    }
}

@Composable
private fun QuestionItem(
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
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
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
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expressive Option Cards & Feedback
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
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
                        modifier = Modifier.weight(1f)
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
                        modifier = Modifier.weight(1f)
                    )
                }

                if (isCompleted) {
                    Indicator(correctAnswer = correct)
                }
            }
        }
    }
}

@Composable
private fun ExpressiveOptionCard(
    title: String,
    iconSelected: ImageVector,
    iconUnselected: ImageVector,
    isChecked: Boolean,
    enabled: Boolean,
    selectedContainerColor: Color,
    selectedContentColor: Color,
    selectedBorderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(
        targetValue = if (isChecked) selectedContainerColor
        else MaterialTheme.colorScheme.surfaceContainerHigh,
        label = "OptionContainerColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isChecked) selectedContentColor
        else MaterialTheme.colorScheme.onSurface,
        label = "OptionContentColor"
    )

    val borderColor = if (isChecked) selectedBorderColor
    else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

    Button(
        onClick = onClick,
        enabled = enabled,
        shapes = ButtonShapes(
            shape = MaterialTheme.shapes.large,
            pressedShape = MaterialTheme.shapes.small
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor
        ),
        border = BorderStroke(if (isChecked) 2.dp else 1.dp, borderColor),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = isChecked,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "OptionIcon"
            ) { checked ->
                Icon(
                    imageVector = if (checked) iconSelected else iconUnselected,
                    contentDescription = null,
                    tint = if (checked) selectedBorderColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.SemiBold,
                color = contentColor,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun QuestionImage(
    image: DrawableResource,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth() .clickable(onClick = onClick),
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
        )

        // Zoom Hint Pill
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
            contentColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ZoomIn,
                    contentDescription = stringResource(Res.string.zoom),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(Res.string.zoom),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun Indicator(
    correctAnswer: Boolean,
) {
    val containerColor = if (correctAnswer) MaterialTheme.semanticColors.successContainer else MaterialTheme.colorScheme.errorContainer
    val contentColor = if (correctAnswer) MaterialTheme.semanticColors.onSuccessContainer else MaterialTheme.colorScheme.onErrorContainer
    val iconColor = if (correctAnswer) MaterialTheme.semanticColors.success else MaterialTheme.colorScheme.error

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = containerColor,
        contentColor = contentColor,
        modifier = Modifier.fillMaxWidth()
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
private fun PagerPreview() {
    MerrPatentenTheme {
        Pager(
            questions = listOf(
                Question(
                    id = 1,
                    question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
                    image = 1,
                    answer = "Saktë"
                )
            ),
            pagerState = rememberPagerState(pageCount = { 1 }),
            falseCheckedPages = listOf(false),
            trueCheckedPages = listOf(true),
            onImageClick = {},
            onCheckFalseAtPage = {},
            onCheckTrueAtPage = {},
            isCompleted = false,
            responses = listOf(0)
        )
    }
}

@Preview
@Composable
private fun PagerCompletedPreview() {
    MerrPatentenTheme {
        Pager(
            questions = listOf(
                Question(
                    id = 1,
                    question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
                    image = 1,
                    answer = "Saktë"
                )
            ),
            pagerState = rememberPagerState(pageCount = { 1 }),
            falseCheckedPages = listOf(false),
            trueCheckedPages = listOf(true),
            onImageClick = {},
            onCheckFalseAtPage = {},
            onCheckTrueAtPage = {},
            isCompleted = true,
            responses = listOf(0)
        )
    }
}

@Preview
@Composable
private fun PagerDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        Pager(
            questions = listOf(
                Question(
                    id = 1,
                    question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
                    image = 1,
                    answer = "Saktë"
                )
            ),
            pagerState = rememberPagerState(pageCount = { 1 }),
            falseCheckedPages = listOf(true),
            trueCheckedPages = listOf(false),
            onImageClick = {},
            onCheckFalseAtPage = {},
            onCheckTrueAtPage = {},
            isCompleted = false,
            responses = listOf(1)
        )
    }
}
