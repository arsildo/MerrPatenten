package com.arsildo.merrpatenten.shared.feature.exam

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.Green
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import com.arsildo.merrpatenten.shared.core.designsystem.formatQuestion
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import com.arsildo.merrpatenten.shared.core.model.Question
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.false_checkbox
import merrpatenten.shared_core.design_system.generated.resources.true_checkbox
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
    responses: List<Int>
) {
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        pageSpacing = 16.dp,
        key = { index -> if (index < questions.size) questions[index].id else index },
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
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QuestionImage(image = image, onClick = onImageClick)
            Text(
                text = formatQuestion(question),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 8.dp)
                    .fillMaxHeight(0.55f),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuestionCheckBox(
                    title = stringResource(Res.string.false_checkbox),
                    checked = falseChecked,
                    enabled = !isCompleted,
                    onCheckedChange = onFalseCheckedChange,
                    checkedColor = Red,
                )
                QuestionCheckBox(
                    title = stringResource(Res.string.true_checkbox),
                    checked = trueChecked,
                    enabled = !isCompleted,
                    onCheckedChange = onTrueCheckedChange,
                    checkedColor = Green,
                )
                if (isCompleted) Indicator(correctAnswer = correct)
            }
        }
    }
}

@Composable
private fun QuestionCheckBox(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean,
    checkedColor: Color,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean
) {
    TextButton(
        onClick = { onCheckedChange(!checked) },
        enabled = enabled,
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (checked) checkedColor else MaterialTheme.colorScheme.primary,
            disabledContentColor = if (checked) checkedColor.copy(0.5f)
            else MaterialTheme.colorScheme.primary.copy(0.5f),
        ),
        contentPadding = if (enabled) PaddingValues(
            end = 24.dp,
            start = 12.dp
        ) else PaddingValues(end = 16.dp),
        modifier = modifier
    ) {
        IconToggleButton(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange,
            colors = IconButtonDefaults.iconToggleButtonColors(
                disabledContentColor = if (checked) checkedColor.copy(0.5f)
                else MaterialTheme.colorScheme.primary.copy(0.5f),
                checkedContentColor = if (checked) checkedColor else MaterialTheme.colorScheme.primary
            )
        ) {
            AnimatedContent(
                targetState = checked,
                label = "",
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
            ) { isChecked ->
                Icon(
                    imageVector = if (isChecked) Icons.Rounded.CheckCircleOutline else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = null,
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun QuestionImage(
    image: DrawableResource,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Image(
        painter = painterResource(image),
        contentDescription = null,
        modifier = Modifier
            .aspectRatio(2f)
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            ),
    )
}

@Composable
private fun Indicator(
    correctAnswer: Boolean,
) {
    val correctColor = Green
    val inCorrectColor = Red
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(if (correctAnswer) correctColor.copy(0.2f) else inCorrectColor.copy(0.2f))
            .padding(vertical = 10.dp, horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (correctAnswer) Icons.Rounded.Check else Icons.Rounded.Clear,
            contentDescription = null,
            tint = if (correctAnswer) correctColor else inCorrectColor
        )
        Text(
            text = stringResource(if (correctAnswer) Res.string.true_checkbox else Res.string.false_checkbox),
            color = if (correctAnswer) correctColor else inCorrectColor,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
