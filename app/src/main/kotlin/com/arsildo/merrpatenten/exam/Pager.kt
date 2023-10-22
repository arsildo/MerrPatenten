package com.arsildo.merrpatenten.exam

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.R
import com.arsildo.merrpatenten.data.Question
import com.arsildo.merrpatenten.theme.Green
import com.arsildo.merrpatenten.theme.Red
import com.arsildo.merrpatenten.utils.formatQuestion
import com.arsildo.merrpatenten.utils.getImageResource

@Suppress("MagicNumber")
@OptIn(ExperimentalFoundationApi::class)
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
        key = { index -> questions[index].id },
    ) { page ->
        Question(
            isCompleted = isCompleted,
            correct = responses[page] == 0,
            image = getImageResource(id = questions[page].image),
            onImageClick = { onImageClick(questions[page].image) },
            question = questions[page].question,
            falseChecked = falseCheckedPages[page],
            trueChecked = trueCheckedPages[page],
            onFalseCheckedChange = { onCheckFalseAtPage(page) },
            onTrueCheckedChange = { onCheckTrueAtPage(page) }
        )
    }
}

@Suppress("MagicNumber")
@Composable
private fun Question(
    isCompleted: Boolean,
    correct: Boolean,
    @DrawableRes image: Int,
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
            .fillMaxHeight(.7f)
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
                    .padding(vertical = 32.dp)
                    .fillMaxHeight(.6f),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuestionCheckBox(
                    title = R.string.false_checkbox,
                    checked = falseChecked,
                    enabled = !isCompleted,
                    onCheckedChange = onFalseCheckedChange,
                    checkedColor = Red,
                )
                QuestionCheckBox(
                    title = R.string.true_checkbox,
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

@Suppress("MagicNumber")
@Composable
private fun QuestionCheckBox(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
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
            disabledContentColor = if (checked) checkedColor.copy(.5f)
            else MaterialTheme.colorScheme.primary.copy(.5f),
        ),
        contentPadding = if (enabled) PaddingValues(
            end = (16 + 16 + 4).dp,
            start = (16 + 4).dp
        ) else PaddingValues(end = 16.dp),
        modifier = modifier
    ) {
        IconToggleButton(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange,
            colors = IconButtonDefaults.iconToggleButtonColors(
                disabledContentColor = if (checked) checkedColor.copy(.5f)
                else MaterialTheme.colorScheme.primary.copy(.5f),
                checkedContentColor = if (checked) checkedColor else MaterialTheme.colorScheme.primary
            )
        ) {
            AnimatedContent(
                targetState = checked,
                label = "",
                transitionSpec = {
                    if (initialState) fadeIn() togetherWith fadeOut() else fadeIn() togetherWith fadeOut()
                }
            ) {
                Icon(
                    imageVector = if (it) Icons.Rounded.CheckCircleOutline else Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = null,
                )
            }
        }
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun QuestionImage(
    @DrawableRes image: Int,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Image(
        painter = painterResource(id = image),
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

@Suppress("MagicNumber")
@Composable
private fun Indicator(
    correctAnswer: Boolean,
    @StringRes inCorrect: Int = R.string.false_checkbox,
    @StringRes correct: Int = R.string.true_checkbox
) {
    val correctColor = Green
    val inCorrectColor = Red
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(if (correctAnswer) correctColor.copy(.2f) else inCorrectColor.copy(.2f))
            .padding(vertical = (8 + 2).dp, horizontal = (16 + 2).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (correctAnswer) Icons.Rounded.Check else Icons.Rounded.Clear,
            contentDescription = null,
            tint = if (correctAnswer) correctColor else inCorrectColor
        )
        Text(
            text = if (correctAnswer) stringResource(id = correct) else stringResource(id = inCorrect),
            color = if (correctAnswer) correctColor else inCorrectColor,
            style = MaterialTheme.typography.titleMedium
        )
    }
}