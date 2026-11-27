package com.arsildo.merrpatenten.shared.feature.mistakes.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.rememberStartToolbarClearance
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.rememberWindowAdaptiveInfo
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.startPadding
import com.arsildo.merrpatenten.shared.core.designsystem.components.CategoryTabs
import com.arsildo.merrpatenten.shared.core.designsystem.components.EmptyStateTone
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExpressiveEmptyState
import com.arsildo.merrpatenten.shared.core.designsystem.components.ResourceImage
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleCheck
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleX
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Play
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Search
import com.arsildo.merrpatenten.shared.core.designsystem.icons.SearchX
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Trash
import com.arsildo.merrpatenten.shared.core.designsystem.icons.X
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.core.model.MistakeQuestion
import com.arsildo.merrpatenten.shared.feature.mistakes.MistakesUiState
import com.arsildo.merrpatenten.shared.feature.mistakes.MistakesViewModel
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MistakesRoute(
    onStartPracticeClick: (category: String) -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    onClearCategoryClick: () -> Unit,
    viewModel: MistakesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MistakesScreen(
        uiState = uiState,
        onStartPracticeClick = { onStartPracticeClick(uiState.selectedCategory) },
        onImageDetailsClick = onImageDetailsClick,
        onSelectCategory = viewModel::onSelectCategory,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onDeleteMistakeClick = viewModel::deleteMistake,
        onClearCategoryClick = onClearCategoryClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MistakesScreen(
    uiState: MistakesUiState,
    onStartPracticeClick: () -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    onSelectCategory: (category: String) -> Unit,
    onSearchQueryChange: (query: String) -> Unit,
    onDeleteMistakeClick: (questionId: Int, category: String) -> Unit,
    onClearCategoryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val adaptiveInfo = rememberWindowAdaptiveInfo()
    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val floatingToolbarClearance = if (adaptiveInfo.isCompact) 88.dp else 0.dp
    val startToolbarClearance = rememberStartToolbarClearance(adaptiveInfo.isCompact)

    val windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)

    Scaffold(
        modifier = modifier
            .startPadding { startToolbarClearance.value },
        topBar = {
            TopAppBar(
                windowInsets = windowInsets,
                title = {
                    Text(
                        text = stringResource(Res.string.mistakes_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                actions = {
                    if (uiState.totalCount > 0) {
                        IconButton(
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onClearCategoryClick()
                            },
                        ) {
                            Icon(
                                imageVector = Trash,
                                contentDescription = stringResource(Res.string.mistakes_clear_category),
                                tint = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = uiState.totalCount > 0,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onStartPracticeClick()
                    },
                    icon = {
                        Icon(
                            imageVector = Play,
                            contentDescription = null,
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(
                                Res.string.mistakes_start_practice,
                                uiState.totalCount,
                            ),
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(bottom = floatingToolbarClearance),
                    elevation = FloatingActionButtonDefaults.loweredElevation(),
                )
            }
        },
        contentWindowInsets = windowInsets,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding),
        ) {
            // Category Tabs
            CategoryTabs(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { category ->
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSelectCategory(category)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            // Search Bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = {
                    Text(
                        text = stringResource(Res.string.catalog_search_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Search,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") },
                            modifier = Modifier.size(24.dp),
                        ) {
                            Icon(
                                imageVector = X,
                                contentDescription = stringResource(Res.string.close),
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            )

            if (uiState.mistakes.isEmpty()) {
                MistakesEmptyView(
                    isFiltered = uiState.searchQuery.isNotBlank(),
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                val deleteItemContentDescription = stringResource(Res.string.mistakes_delete_item)
                val correctAnswerPrefix = stringResource(Res.string.correct_answer_feedback)
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 340.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 96.dp + floatingToolbarClearance + navBarBottomPadding,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(
                        items = uiState.mistakes,
                        key = { it.question.id },
                        contentType = { "mistake" },
                    ) { item ->
                        MistakeQuestionCard(
                            item = item,
                            deleteItemContentDescription = deleteItemContentDescription,
                            correctAnswerPrefix = correctAnswerPrefix,
                            onImageClick = { onImageDetailsClick(item.question.image) },
                            onDeleteClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onDeleteMistakeClick(item.question.id, item.question.category)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MistakeQuestionCard(
    item: MistakeQuestion,
    deleteItemContentDescription: String,
    correctAnswerPrefix: String,
    onImageClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Header: Category/ID badge, Error count, Mastery progress, Delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = "${item.question.category} #${item.question.id}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )

                    // Error count badge (Red)
                    Badge(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    ) {
                        Text(
                            text = stringResource(Res.string.mistakes_error_count, item.record.errorCount),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        )
                    }

                    // Mastery progress badge (Green)
                    Badge(
                        containerColor = MaterialTheme.semanticColors.successContainer,
                        contentColor = MaterialTheme.semanticColors.onSuccessContainer,
                    ) {
                        Text(
                            text = stringResource(Res.string.mistakes_mastery_progress, item.record.consecutiveCorrect, 2),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        )
                    }
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        imageVector = X,
                        contentDescription = deleteItemContentDescription,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Image Thumbnail (if present)
            if (item.question.image > 0) {
                ResourceImage(
                    imageId = item.question.image,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onImageClick),
                )
            }

            // Question Text
            Text(
                text = item.question.question,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            // Official Answer Banner (Green for Saktë, Red for Gabim)
            val isTrue = item.question.answer.trim().equals("Saktë", ignoreCase = true)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isTrue) {
                    MaterialTheme.semanticColors.successContainer
                } else {
                    MaterialTheme.colorScheme.errorContainer
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = if (isTrue) CircleCheck else CircleX,
                        contentDescription = null,
                        tint = if (isTrue) MaterialTheme.semanticColors.success else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = "$correctAnswerPrefix: ${item.question.answer}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isTrue) {
                            MaterialTheme.semanticColors.onSuccessContainer
                        } else {
                            MaterialTheme.colorScheme.onErrorContainer
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun MistakesEmptyView(isFiltered: Boolean, modifier: Modifier = Modifier) {
    ExpressiveEmptyState(
        icon = if (isFiltered) SearchX else CircleCheck,
        title = stringResource(
            if (isFiltered) Res.string.catalog_empty_results else Res.string.mistakes_empty_title,
        ),
        description = if (isFiltered) null else stringResource(Res.string.mistakes_empty_desc),
        tone = if (isFiltered) EmptyStateTone.Search else EmptyStateTone.Celebratory,
        modifier = modifier,
    )
}
