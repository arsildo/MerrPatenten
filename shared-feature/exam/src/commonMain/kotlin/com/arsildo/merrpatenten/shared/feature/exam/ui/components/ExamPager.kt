package com.arsildo.merrpatenten.shared.feature.exam.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.QuestionTextSize
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import com.arsildo.merrpatenten.shared.core.model.Question

@Composable
internal fun ExamPager(
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
        modifier = modifier.fillMaxSize(),
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
                onTrueCheckedChange = { onCheckTrueAtPage(page) },
            )
        }
    }
}

@Preview
@Composable
private fun ExamPagerPreview() {
    MerrPatentenTheme {
        ExamPager(
            questions = listOf(
                Question(
                    id = 1,
                    question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
                    image = 1,
                    answer = "Saktë",
                ),
            ),
            pagerState = rememberPagerState(pageCount = { 1 }),
            falseCheckedPages = listOf(false),
            trueCheckedPages = listOf(true),
            onImageClick = {},
            onCheckFalseAtPage = {},
            onCheckTrueAtPage = {},
            isCompleted = false,
            responses = listOf(0),
        )
    }
}

@Preview
@Composable
private fun ExamPagerCompletedPreview() {
    MerrPatentenTheme {
        ExamPager(
            questions = listOf(
                Question(
                    id = 1,
                    question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
                    image = 1,
                    answer = "Saktë",
                ),
            ),
            pagerState = rememberPagerState(pageCount = { 1 }),
            falseCheckedPages = listOf(false),
            trueCheckedPages = listOf(true),
            onImageClick = {},
            onCheckFalseAtPage = {},
            onCheckTrueAtPage = {},
            isCompleted = true,
            responses = listOf(0),
        )
    }
}

@Preview
@Composable
private fun ExamPagerDarkPreview() {
    MerrPatentenTheme(darkTheme = true) {
        ExamPager(
            questions = listOf(
                Question(
                    id = 1,
                    question = "Sinjali i paraqitur në figurë tregon një kthesë të rrezikshme majtas.",
                    image = 1,
                    answer = "Saktë",
                ),
            ),
            pagerState = rememberPagerState(pageCount = { 1 }),
            falseCheckedPages = listOf(true),
            trueCheckedPages = listOf(false),
            onImageClick = {},
            onCheckFalseAtPage = {},
            onCheckTrueAtPage = {},
            isCompleted = false,
            responses = listOf(1),
        )
    }
}
