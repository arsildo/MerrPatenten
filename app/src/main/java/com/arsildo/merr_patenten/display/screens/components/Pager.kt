package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arsildo.merr_patenten.display.theme.Black
import com.arsildo.merr_patenten.display.theme.Green
import com.arsildo.merr_patenten.display.theme.Red
import com.arsildo.merr_patenten.logic.database.DatabaseModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Pager(
    pagerState: PagerState,
    isExamCompleted: Boolean,
    list: List<DatabaseModel>,
    mistakePositions: List<Int>,
    trueCheckedPositions: List<Boolean>,
    falseCheckedPositions: List<Boolean>,
    checkFalseAt: (Int) -> Unit,
    checkTrueAt: (Int) -> Unit,
) {
    HorizontalPager(
        count = 40,
        state = pagerState,
    ) { page ->
        PagerItem(
            model = list[page],
            isExamCompleted = isExamCompleted,
            mistakePosition = mistakePositions[page],
            trueChecked = trueCheckedPositions[page],
            falseChecked = falseCheckedPositions[page],
            onFalseChecked = { if (!isExamCompleted) checkFalseAt(page) },
            onTrueChecked = { if (!isExamCompleted) checkTrueAt(page) },
        )
    }
}

@Composable
fun PagerItem(
    model: DatabaseModel,
    isExamCompleted: Boolean,
    mistakePosition: Int,
    trueChecked: Boolean,
    falseChecked: Boolean,
    onFalseChecked: (Boolean) -> Unit,
    onTrueChecked: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(id = model.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(.5f)
                .height(256.dp)
                .padding(bottom = 32.dp)
        )
        Text(
            text = model.question,
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier.padding(vertical = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(if (isExamCompleted) 32.dp else 64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PagerCheckbox(
                title = "Gabim",
                checked = falseChecked,
                titleColor = if (falseChecked) if (isExamCompleted) Red.copy(.4f) else Red else if (isExamCompleted) Black.copy(
                    .4f
                ) else MaterialTheme.colors.onBackground,
                checkedColor = if (falseChecked) Red else Black,
                enabled = !isExamCompleted,
                onCheckedChanged = onFalseChecked
            )
            PagerCheckbox(
                title = "Saktë",
                titleColor = if (trueChecked) if (isExamCompleted) Green.copy(.4f) else Green else if (isExamCompleted) Black.copy(
                    .4f
                ) else MaterialTheme.colors.onBackground,
                checked = trueChecked,
                checkedColor = if (trueChecked) Green else Black,
                enabled = !isExamCompleted,
                onCheckedChanged = onTrueChecked
            )
            if (isExamCompleted) {
                Indicator(correct = mistakePosition == 0)
            }

        }
    }
}

@Composable
fun PagerCheckbox(
    title: String,
    titleColor: Color,
    checked: Boolean,
    checkedColor: Color,
    enabled: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { if (enabled) onCheckedChanged(!checked) }
            .padding(start = 0.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChanged,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = checkedColor,
                uncheckedColor = MaterialTheme.colors.onBackground,
                disabledColor = checkedColor.copy(.4f)
            )
        )
        Text(
            text = title,
            color = titleColor
        )
    }
}

@Composable
fun Indicator(correct: Boolean) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(if (correct) Green.copy(.15f) else Red.copy(.15f))
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            if (correct) Icons.Rounded.Check else Icons.Rounded.Clear,
            contentDescription = null,
            tint = if (correct) Green else Red,
            modifier = Modifier.size((16 + 8).dp)

        )
        Text(
            text = if (correct) "Saktë" else "Gabim",
            color = if (correct) Green else Red,
            fontSize = 16.sp
        )
    }
}