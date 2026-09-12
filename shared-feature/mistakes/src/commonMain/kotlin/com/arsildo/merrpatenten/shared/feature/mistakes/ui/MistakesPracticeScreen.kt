package com.arsildo.merrpatenten.shared.feature.mistakes.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.feature.mistakes.MistakesPracticeUiState
import com.arsildo.merrpatenten.shared.feature.mistakes.MistakesPracticeViewModel
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MistakesPracticeRoute(
    category: String,
    onBackPress: () -> Unit,
    onImageDetailsClick: (Int) -> Unit,
    viewModel: MistakesPracticeViewModel = koinViewModel(parameters = { parametersOf(category) }),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MistakesPracticeScreen(
        uiState = uiState,
        onBackPress = onBackPress,
        onSubmitAnswer = viewModel::submitAnswer,
        onNextQuestion = viewModel::nextQuestion,
        onImageDetailsClick = onImageDetailsClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MistakesPracticeScreen(
    uiState: MistakesPracticeUiState,
    onBackPress: () -> Unit,
    onSubmitAnswer: (String) -> Unit,
    onNextQuestion: () -> Unit,
    onImageDetailsClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.mistakes_practice_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onBackPress()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { contentPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.questions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Celebration,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp),
                    )
                    Text(
                        text = stringResource(Res.string.mistakes_empty_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Button(onClick = onBackPress) {
                        Text(stringResource(Res.string.back))
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
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Progress Bar & Step Info
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

                    // Question Card (Flat Card with surfaceContainerLow matching Exam QuestionItem)
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
                            // Status Badges (Red for Error count, Green for Mastery progress)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                                ) {
                                    Text(
                                        text = stringResource(
                                            Res.string.mistakes_error_count,
                                            currentItem.record.errorCount,
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    )
                                }

                                Badge(
                                    containerColor = MaterialTheme.semanticColors.successContainer,
                                    contentColor = MaterialTheme.semanticColors.onSuccessContainer,
                                ) {
                                    Text(
                                        text = stringResource(
                                            Res.string.mistakes_mastery_progress,
                                            currentItem.record.consecutiveCorrect,
                                            2,
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    )
                                }
                            }

                            // Sign Image (if present)
                            if (currentItem.question.image > 0) {
                                val imageRes = getImageResource(currentItem.question.image)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
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

                            // Question Text
                            Text(
                                text = currentItem.question.question,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }

                    // Answer Options (Gabim / Saktë - Left to Right matching Exam screen layout and styling)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        PracticeOptionButton(
                            title = stringResource(Res.string.false_checkbox),
                            iconSelected = Icons.Rounded.Cancel,
                            iconUnselected = Icons.Outlined.Cancel,
                            isSelected = uiState.selectedAnswer == "Gabim",
                            isRevealed = uiState.isAnswerRevealed,
                            isCorrectOption = currentItem.question.answer.trim().equals("Gabim", ignoreCase = true),
                            selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                            selectedContentColor = MaterialTheme.colorScheme.onErrorContainer,
                            selectedBorderColor = MaterialTheme.colorScheme.error,
                            onClick = {
                                onSubmitAnswer("Gabim")
                            },
                            modifier = Modifier.weight(1f),
                        )

                        PracticeOptionButton(
                            title = stringResource(Res.string.true_checkbox),
                            iconSelected = Icons.Rounded.CheckCircle,
                            iconUnselected = Icons.Outlined.CheckCircle,
                            isSelected = uiState.selectedAnswer == "Saktë",
                            isRevealed = uiState.isAnswerRevealed,
                            isCorrectOption = currentItem.question.answer.trim().equals("Saktë", ignoreCase = true),
                            selectedContainerColor = MaterialTheme.semanticColors.successContainer,
                            selectedContentColor = MaterialTheme.semanticColors.onSuccessContainer,
                            selectedBorderColor = MaterialTheme.semanticColors.success,
                            onClick = {
                                onSubmitAnswer("Saktë")
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }

                    // Feedback Banner (Revealed after answer)
                    AnimatedVisibility(
                        visible = uiState.isAnswerRevealed,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (uiState.isAnswerCorrect) {
                                    MaterialTheme.semanticColors.successContainer
                                } else {
                                    MaterialTheme.colorScheme.errorContainer
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = if (uiState.isAnswerCorrect) {
                                            Icons.Rounded.CheckCircle
                                        } else {
                                            Icons.Rounded.Cancel
                                        },
                                        contentDescription = null,
                                        tint = if (uiState.isAnswerCorrect) {
                                            MaterialTheme.semanticColors.success
                                        } else {
                                            MaterialTheme.colorScheme.error
                                        },
                                        modifier = Modifier.size(28.dp),
                                    )
                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                            text = if (uiState.isAnswerCorrect) {
                                                if (uiState.hasGraduated) {
                                                    stringResource(Res.string.mistakes_practice_graduated)
                                                } else {
                                                    stringResource(Res.string.mistakes_practice_correct_streak, 1, 2)
                                                }
                                            } else {
                                                stringResource(Res.string.mistakes_practice_wrong)
                                            },
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (uiState.isAnswerCorrect) {
                                                MaterialTheme.semanticColors.onSuccessContainer
                                            } else {
                                                MaterialTheme.colorScheme.onErrorContainer
                                            },
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onNextQuestion()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(16.dp),
                            ) {
                                Text(
                                    text = if (uiState.currentIndex + 1 >= uiState.questions.size) {
                                        stringResource(Res.string.mistakes_practice_finish)
                                    } else {
                                        stringResource(Res.string.mistakes_practice_next)
                                    },
                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (uiState.isPracticeFinished && uiState.questions.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = onBackPress,
            icon = {
                Icon(
                    imageVector = Icons.Rounded.EmojiEvents,
                    contentDescription = null,
                    tint = MaterialTheme.semanticColors.success,
                    modifier = Modifier.size(48.dp),
                )
            },
            title = {
                Text(
                    text = stringResource(Res.string.mistakes_practice_complete_title),
                    fontWeight = FontWeight.Bold,
                )
            },
            text = {
                Text(
                    text = stringResource(
                        Res.string.mistakes_practice_complete_desc,
                        uiState.totalAnsweredCount,
                        uiState.masteredQuestionsCount,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            confirmButton = {
                Button(
                    onClick = onBackPress,
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(stringResource(Res.string.results_confirm))
                }
            },
        )
    }
}

@Composable
private fun PracticeOptionButton(
    title: String,
    iconSelected: ImageVector,
    iconUnselected: ImageVector,
    isSelected: Boolean,
    isRevealed: Boolean,
    isCorrectOption: Boolean,
    selectedContainerColor: androidx.compose.ui.graphics.Color,
    selectedContentColor: androidx.compose.ui.graphics.Color,
    selectedBorderColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val isHighlightedAsCorrect = isRevealed && isCorrectOption
    val isHighlightedAsWrong = isRevealed && isSelected && !isCorrectOption

    val containerColor by animateColorAsState(
        targetValue = when {
            isHighlightedAsCorrect -> MaterialTheme.semanticColors.successContainer
            isHighlightedAsWrong -> MaterialTheme.colorScheme.errorContainer
            isSelected -> selectedContainerColor
            else -> MaterialTheme.colorScheme.surfaceContainerHigh
        },
        label = "PracticeOptionContainerColor",
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            isHighlightedAsCorrect -> MaterialTheme.semanticColors.onSuccessContainer
            isHighlightedAsWrong -> MaterialTheme.colorScheme.onErrorContainer
            isSelected -> selectedContentColor
            else -> MaterialTheme.colorScheme.onSurface
        },
        label = "PracticeOptionContentColor",
    )

    val borderColor = when {
        isHighlightedAsCorrect -> MaterialTheme.semanticColors.success
        isHighlightedAsWrong -> MaterialTheme.colorScheme.error
        isSelected -> selectedBorderColor
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    }

    val iconVector = when {
        isHighlightedAsCorrect -> Icons.Rounded.CheckCircle
        isHighlightedAsWrong -> Icons.Rounded.Cancel
        isSelected -> iconSelected
        else -> iconUnselected
    }

    val iconTint = when {
        isHighlightedAsCorrect -> MaterialTheme.semanticColors.success
        isHighlightedAsWrong -> MaterialTheme.colorScheme.error
        isSelected -> selectedBorderColor
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Button(
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            onClick()
        },
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
        border = BorderStroke(if (isSelected || isHighlightedAsCorrect) 2.dp else 1.dp, borderColor),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            AnimatedContent(
                targetState = iconVector,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "PracticeOptionIcon",
            ) { targetIcon ->
                Icon(
                    imageVector = targetIcon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp),
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isSelected || isHighlightedAsCorrect) FontWeight.Bold else FontWeight.SemiBold,
                color = contentColor,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}
