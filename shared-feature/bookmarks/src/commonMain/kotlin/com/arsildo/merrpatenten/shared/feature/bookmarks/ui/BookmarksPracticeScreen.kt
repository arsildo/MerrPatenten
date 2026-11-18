package com.arsildo.merrpatenten.shared.feature.bookmarks.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import com.arsildo.merrpatenten.shared.core.designsystem.icons.ArrowLeft
import com.arsildo.merrpatenten.shared.core.designsystem.icons.ArrowRight
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleCheck
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleX
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Star
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.feature.bookmarks.BookmarksPracticeUiState
import com.arsildo.merrpatenten.shared.feature.bookmarks.BookmarksPracticeViewModel
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BookmarksPracticeRoute(
    category: String,
    onBackPress: () -> Unit,
    onPracticeFinished: () -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    viewModel: BookmarksPracticeViewModel = koinViewModel(parameters = { parametersOf(category) }),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookmarksPracticeScreen(
        uiState = uiState,
        onBackPress = onBackPress,
        onSubmitAnswer = viewModel::submitAnswer,
        onNextQuestion = {
            if (uiState.currentIndex + 1 >= uiState.questions.size) onPracticeFinished()
            viewModel.nextQuestion()
        },
        onImageDetailsClick = onImageDetailsClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BookmarksPracticeScreen(
    uiState: BookmarksPracticeUiState,
    onBackPress: () -> Unit,
    onSubmitAnswer: (answer: String) -> Unit,
    onNextQuestion: () -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)

    Scaffold(
        modifier = modifier,
        contentWindowInsets = windowInsets,
        topBar = {
            TopAppBar(
                windowInsets = windowInsets,
                title = {
                    Text(
                        text = stringResource(Res.string.bookmarks_practice_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    FilledTonalIconButton(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onBackPress()
                        },
                        shapes = IconButtonDefaults.shapes(),
                    ) {
                        Icon(
                            imageVector = ArrowLeft,
                            contentDescription = stringResource(Res.string.back),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.questions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .consumeWindowInsets(contentPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Icon(
                        imageVector = Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp),
                    )
                    Text(
                        text = stringResource(Res.string.bookmarks_empty_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Button(
                        onClick = onBackPress,
                        shapes = ButtonShapes(
                            shape = MaterialTheme.shapes.large,
                            pressedShape = MaterialTheme.shapes.small,
                        ),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp),
                    ) {
                        Text(
                            text = stringResource(Res.string.back),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        } else {
            val currentItem = uiState.currentQuestion
            if (currentItem != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .consumeWindowInsets(contentPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                        .padding(top = 12.dp, bottom = 24.dp)
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    val progress = (uiState.currentIndex + 1).toFloat() / uiState.questions.size.toFloat()
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(
                                    Res.string.question_number_format,
                                    uiState.currentIndex + 1,
                                ) + " / ${uiState.questions.size}",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "${uiState.category} #${currentItem.question.id}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.outline,
                            )
                        }
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                        )
                    }

                    Card(
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            if (currentItem.question.image > 0) {
                                val imageRes = getImageResource(currentItem.question.image)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { onImageDetailsClick(currentItem.question.image) },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Image(
                                        painter = painterResource(imageRes),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }
                            }

                            Text(
                                text = currentItem.question.question,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        PracticeAnswerButton(
                            label = stringResource(Res.string.true_checkbox),
                            icon = CircleCheck,
                            isSelected = uiState.selectedAnswer == "Saktë",
                            isCorrectTarget = currentItem.question.answer.equals("Saktë", ignoreCase = true),
                            isRevealed = uiState.isAnswerRevealed,
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onSubmitAnswer("Saktë")
                            },
                            modifier = Modifier.weight(1f),
                        )

                        PracticeAnswerButton(
                            label = stringResource(Res.string.false_checkbox),
                            icon = CircleX,
                            isSelected = uiState.selectedAnswer == "Gabim",
                            isCorrectTarget = currentItem.question.answer.equals("Gabim", ignoreCase = true),
                            isRevealed = uiState.isAnswerRevealed,
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onSubmitAnswer("Gabim")
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }

                    AnimatedVisibility(
                        visible = uiState.isAnswerRevealed,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            val bannerContainerColor = if (uiState.isAnswerCorrect) {
                                MaterialTheme.semanticColors.successContainer
                            } else {
                                MaterialTheme.colorScheme.errorContainer
                            }
                            val bannerContentColor = if (uiState.isAnswerCorrect) {
                                MaterialTheme.semanticColors.onSuccessContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            }

                            Surface(
                                shape = MaterialTheme.shapes.large,
                                color = bannerContainerColor,
                                contentColor = bannerContentColor,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = if (uiState.isAnswerCorrect) CircleCheck else CircleX,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                    )
                                    Text(
                                        text = if (uiState.isAnswerCorrect) {
                                            stringResource(Res.string.bookmarks_practice_correct)
                                        } else {
                                            stringResource(
                                                Res.string.bookmarks_practice_wrong,
                                                currentItem.question.answer,
                                            )
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onNextQuestion()
                                },
                                shapes = ButtonShapes(
                                    shape = MaterialTheme.shapes.large,
                                    pressedShape = MaterialTheme.shapes.small,
                                ),
                                contentPadding = PaddingValues(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                ),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                AnimatedContent(
                                    targetState = uiState.currentIndex + 1 >= uiState.questions.size,
                                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                                    label = "NextButtonLabel",
                                ) { isLast ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = stringResource(
                                                if (isLast) {
                                                    Res.string.mistakes_practice_finish
                                                } else {
                                                    Res.string.mistakes_practice_next
                                                },
                                            ),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                        Icon(
                                            imageVector = ArrowRight,
                                            contentDescription = null,
                                            modifier = Modifier.size(22.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun PracticeAnswerButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    isCorrectTarget: Boolean,
    isRevealed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor by animateColorAsState(
        targetValue = when {
            !isRevealed -> MaterialTheme.colorScheme.surfaceContainerHigh
            isCorrectTarget -> MaterialTheme.semanticColors.successContainer
            isSelected -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f)
        },
        label = "AnswerButtonContainer",
    )
    val contentColor by animateColorAsState(
        targetValue = when {
            !isRevealed -> MaterialTheme.colorScheme.onSurface
            isCorrectTarget -> MaterialTheme.semanticColors.onSuccessContainer
            isSelected -> MaterialTheme.colorScheme.onErrorContainer
            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        },
        label = "AnswerButtonContent",
    )

    Button(
        onClick = onClick,
        enabled = !isRevealed,
        shapes = ButtonShapes(
            shape = MaterialTheme.shapes.large,
            pressedShape = MaterialTheme.shapes.small,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor,
        ),
        border = BorderStroke(
            width = if (isRevealed && (isSelected || isCorrectTarget)) 2.dp else 1.dp,
            color = when {
                !isRevealed -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                isCorrectTarget -> MaterialTheme.semanticColors.success
                isSelected -> MaterialTheme.colorScheme.error
                else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            },
        ),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = if (isRevealed && (isSelected || isCorrectTarget)) FontWeight.Bold else FontWeight.SemiBold,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}
