package com.arsildo.merrpatenten.shared.feature.exam

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.Green
import com.arsildo.merrpatenten.shared.core.designsystem.GreenContainer
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.OnGreenContainer
import com.arsildo.merrpatenten.shared.core.designsystem.OnRedContainer
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import com.arsildo.merrpatenten.shared.core.designsystem.RedContainer
import com.arsildo.merrpatenten.shared.core.designsystem.formatQuestion
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
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
                correct = if (page < responses.size) responses[page] == 0 else false,
                image = getImageResource(id = questions[page].image),
                onImageClick = { onImageClick(questions[page].image) },
                question = questions[page].question,
                falseChecked = if (page < falseCheckedPages.size) falseCheckedPages[page] else false,
                trueChecked = if (page < trueCheckedPages.size) trueCheckedPages[page] else false,
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
    onFalseCheckedChange: (Boolean) -> Unit,
    onTrueCheckedChange: (Boolean) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
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
                    style = MaterialTheme.typography.titleMedium,
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
                        selectedContainerColor = RedContainer,
                        selectedContentColor = OnRedContainer,
                        selectedBorderColor = Red,
                        onClick = { onFalseCheckedChange(!falseChecked) },
                        modifier = Modifier.weight(1f)
                    )

                    ExpressiveOptionCard(
                        title = stringResource(Res.string.true_checkbox),
                        iconSelected = Icons.Rounded.CheckCircle,
                        iconUnselected = Icons.Outlined.CheckCircle,
                        isChecked = trueChecked,
                        enabled = !isCompleted,
                        selectedContainerColor = GreenContainer,
                        selectedContentColor = OnGreenContainer,
                        selectedBorderColor = Green,
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
        contentPadding = PaddingValues(vertical = 14.dp, horizontal = 12.dp),
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
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .padding(8.dp)
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
}

@Composable
private fun Indicator(
    correctAnswer: Boolean,
) {
    val containerColor = if (correctAnswer) GreenContainer else RedContainer
    val contentColor = if (correctAnswer) OnGreenContainer else OnRedContainer
    val iconColor = if (correctAnswer) Green else Red

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
