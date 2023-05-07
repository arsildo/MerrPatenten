package com.arsildo.merr_patenten.exam

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arsildo.merr_patenten.R
import com.arsildo.merr_patenten.data.Question
import com.arsildo.merr_patenten.theme.Green
import com.arsildo.merr_patenten.theme.Red
import com.arsildo.merr_patenten.utils.getImageResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Pager(
    questions: List<Question>,
    pagerState: PagerState,
    isExamCompleted: Boolean,
) {
    HorizontalPager(
        pageCount = 40,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        pageSpacing = 16.dp,
    ) { page ->
        Question(
            image = getImageResource(questions[page].id),
            question = questions[page].question,
            falseChecked = true,
            trueChecked = false,
            onFalseCheckedChange = {},
            onTrueCheckedChange = {},
            isExamCompleted = isExamCompleted
        )
    }
}


@Stable
private val correctColor = Green

@Stable
private val inCorrectColor = Red

@Composable
private fun Question(
    @DrawableRes image: Int,
    question: String,
    falseChecked: Boolean,
    trueChecked: Boolean,
    onFalseCheckedChange: (Boolean) -> Unit,
    onTrueCheckedChange: (Boolean) -> Unit,
    isExamCompleted: Boolean,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.6f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.aspectRatio(1.8f)
            )
            Text(
                text = question,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 32.dp),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                QuestionCheckBox(
                    title = R.string.falseCheckBox,
                    checked = falseChecked,
                    enabled = isExamCompleted,
                    onCheckedChange = onFalseCheckedChange,
                    checkedColor = inCorrectColor
                )
                QuestionCheckBox(
                    title = R.string.trueCheckBox,
                    checked = trueChecked,
                    enabled = isExamCompleted,
                    onCheckedChange = onTrueCheckedChange,
                    checkedColor = correctColor
                )
                Indicator(correctAnswer = true)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun QuestionCheckBox(
    @StringRes title: Int,
    checked: Boolean,
    checkedColor: Color,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean,
) {
    TextButton(
        onClick = { onCheckedChange(!checked) },
        enabled = enabled,
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.textButtonColors(
            contentColor = if (checked) checkedColor else MaterialTheme.colorScheme.primary,
            disabledContentColor = if (checked) checkedColor.copy(.5f) else MaterialTheme.colorScheme.primary.copy(
                .5f
            ),
        ),
        contentPadding = PaddingValues(end = (16 + 2).dp)
    ) {
        IconToggleButton(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange,
            colors = IconButtonDefaults.iconToggleButtonColors(
                disabledContentColor = if (checked) checkedColor.copy(.5f) else MaterialTheme.colorScheme.primary.copy(
                    .5f
                ),
                checkedContentColor = if (checked) checkedColor else MaterialTheme.colorScheme.primary
            )
        ) {
            AnimatedContent(
                targetState = checked,
                label = "",
                transitionSpec = { if (initialState) fadeIn() with fadeOut() else fadeIn() with fadeOut() }
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
private fun Indicator(
    correctAnswer: Boolean,
    @StringRes inCorrect: Int = R.string.falseCheckBox,
    @StringRes correct: Int = R.string.trueCheckBox
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                if (correctAnswer) correctColor.copy(.2f) else inCorrectColor.copy(.2f)
            )
            .padding(vertical = (8 + 2).dp, horizontal = (16 + 2).dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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