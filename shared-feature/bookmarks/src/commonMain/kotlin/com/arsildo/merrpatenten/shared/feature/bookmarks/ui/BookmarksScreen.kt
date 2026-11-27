package com.arsildo.merrpatenten.shared.feature.bookmarks.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.components.CategoryTabs
import com.arsildo.merrpatenten.shared.core.designsystem.components.EmptyStateTone
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExpressiveEmptyState
import com.arsildo.merrpatenten.shared.core.designsystem.components.ResourceImage
import com.arsildo.merrpatenten.shared.core.designsystem.formatQuestion
import com.arsildo.merrpatenten.shared.core.designsystem.icons.*
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.core.model.BookmarkedQuestion
import com.arsildo.merrpatenten.shared.feature.bookmarks.BookmarksUiState
import com.arsildo.merrpatenten.shared.feature.bookmarks.BookmarksViewModel
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun BookmarksRoute(
    viewModel: BookmarksViewModel = koinViewModel(),
    onBackPress: () -> Unit,
    onStartPractice: (category: String) -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    onClearCategoryClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookmarksScreen(
        uiState = uiState,
        onBackPress = onBackPress,
        onStartPractice = { onStartPractice(uiState.selectedCategory) },
        onImageDetailsClick = onImageDetailsClick,
        onSelectCategory = viewModel::onSelectCategory,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onRemoveBookmark = viewModel::removeBookmark,
        onClearCategoryClick = onClearCategoryClick,
    )
}

@Composable
private fun BookmarksScreen(
    modifier: Modifier = Modifier,
    uiState: BookmarksUiState,
    onBackPress: () -> Unit,
    onStartPractice: () -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    onSelectCategory: (category: String) -> Unit,
    onSearchQueryChange: (query: String) -> Unit,
    onRemoveBookmark: (questionId: Int, category: String) -> Unit,
    onClearCategoryClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                windowInsets = windowInsets,
                title = {
                    Text(
                        text = stringResource(Res.string.bookmarks_title),
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
                actions = {
                    if (uiState.totalCount > 0) {
                        FilledTonalIconButton(
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onClearCategoryClick()
                            },
                            shapes = IconButtonDefaults.shapes(),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            ),
                        ) {
                            Icon(
                                imageVector = Trash,
                                contentDescription = stringResource(Res.string.bookmarks_clear_category),
                            )
                        }
                    }
                },
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = uiState.totalCount > 0,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Button(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onStartPractice()
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Play,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                        )
                        Text(
                            text = stringResource(
                                Res.string.bookmarks_start_practice,
                                uiState.totalCount,
                            ),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
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

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = {
                    Text(
                        text = stringResource(Res.string.bookmarks_search_placeholder),
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
                            shapes = IconButtonDefaults.shapes(),
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

            if (uiState.bookmarks.isEmpty() && !uiState.isLoading) {
                BookmarksEmptyView(
                    isFiltered = uiState.searchQuery.isNotBlank(),
                    modifier = Modifier.fillMaxSize(),
                )
            } else if (uiState.bookmarks.isNotEmpty()) {
                val removeBookmarkContentDescription = stringResource(Res.string.bookmarks_toggle_remove)
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 340.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 96.dp + navBarBottomPadding,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(
                        items = uiState.bookmarks,
                        key = { it.question.id },
                        contentType = { "bookmark" },
                    ) { item ->
                        BookmarkedQuestionCard(
                            item = item,
                            removeBookmarkContentDescription = removeBookmarkContentDescription,
                            onImageClick = { onImageDetailsClick(item.question.image) },
                            onRemoveClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onRemoveBookmark(item.question.id, item.question.category)
                            },
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookmarkedQuestionCard(
    modifier: Modifier = Modifier,
    item: BookmarkedQuestion,
    removeBookmarkContentDescription: String,
    onImageClick: () -> Unit,
    onRemoveClick: () -> Unit,
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

                    val isTrue = item.question.answer.trim().equals("Saktë", ignoreCase = true)
                    Badge(
                        containerColor = if (isTrue) {
                            MaterialTheme.semanticColors.successContainer
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        },
                        contentColor = if (isTrue) {
                            MaterialTheme.semanticColors.onSuccessContainer
                        } else {
                            MaterialTheme.colorScheme.onErrorContainer
                        },
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = if (isTrue) CircleCheck else CircleX,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp),
                            )
                            Text(
                                text = item.question.answer,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }

                FilledTonalIconButton(
                    onClick = onRemoveClick,
                    shapes = IconButtonDefaults.shapes(),
                    modifier = Modifier.size(36.dp),
                ) {
                    Icon(
                        imageVector = StarX,
                        contentDescription = removeBookmarkContentDescription,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

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

            val formattedQuestion = remember(item.question.question) {
                formatQuestion(item.question.question)
            }
            Text(
                text = formattedQuestion,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun BookmarksEmptyView(modifier: Modifier = Modifier, isFiltered: Boolean) {
    ExpressiveEmptyState(
        icon = if (isFiltered) SearchX else Star,
        title = stringResource(
            if (isFiltered) Res.string.bookmarks_empty_results else Res.string.bookmarks_empty_title,
        ),
        description = if (isFiltered) null else stringResource(Res.string.bookmarks_empty_desc),
        tone = if (isFiltered) EmptyStateTone.Search else EmptyStateTone.Default,
        modifier = modifier,
    )
}
