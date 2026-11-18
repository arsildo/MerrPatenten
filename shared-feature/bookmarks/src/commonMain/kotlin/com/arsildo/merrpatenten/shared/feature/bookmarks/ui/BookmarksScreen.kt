package com.arsildo.merrpatenten.shared.feature.bookmarks.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import com.arsildo.merrpatenten.shared.core.designsystem.icons.ArrowLeft
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleCheck
import com.arsildo.merrpatenten.shared.core.designsystem.icons.CircleX
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Play
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Search
import com.arsildo.merrpatenten.shared.core.designsystem.icons.SearchX
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Star
import com.arsildo.merrpatenten.shared.core.designsystem.icons.StarX
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Trash
import com.arsildo.merrpatenten.shared.core.designsystem.icons.X
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.core.model.BookmarkedQuestion
import com.arsildo.merrpatenten.shared.feature.bookmarks.BookmarksUiState
import com.arsildo.merrpatenten.shared.feature.bookmarks.BookmarksViewModel
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookmarksRoute(
    onBackPress: () -> Unit,
    onStartPractice: (category: String) -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    onClearCategoryClick: () -> Unit,
    viewModel: BookmarksViewModel = koinViewModel(),
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BookmarksScreen(
    uiState: BookmarksUiState,
    onBackPress: () -> Unit,
    onStartPractice: () -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    onSelectCategory: (category: String) -> Unit,
    onSearchQueryChange: (query: String) -> Unit,
    onRemoveBookmark: (questionId: Int, category: String) -> Unit,
    onClearCategoryClick: () -> Unit,
    modifier: Modifier = Modifier,
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

            if (uiState.bookmarks.isEmpty()) {
                BookmarksEmptyView(
                    isFiltered = uiState.searchQuery.isNotBlank(),
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
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
                        key = { "${it.question.id}_${it.question.category}" },
                    ) { item ->
                        BookmarkedQuestionCard(
                            item = item,
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
private fun CategoryTabs(selectedCategory: String, onCategorySelected: (category: String) -> Unit, modifier: Modifier = Modifier) {
    val categories = listOf("B", "C", "D")

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        categories.forEachIndexed { index, catKey ->
            SegmentedButton(
                selected = selectedCategory == catKey,
                onClick = { onCategorySelected(catKey) },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = categories.size),
            ) {
                Text(
                    text = catKey,
                    fontWeight = if (selectedCategory == catKey) FontWeight.Bold else FontWeight.Normal,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BookmarkedQuestionCard(
    item: BookmarkedQuestion,
    onImageClick: () -> Unit,
    onRemoveClick: () -> Unit,
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

                    val isTrue = item.question.answer.equals("Saktë", ignoreCase = true)
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
                        contentDescription = stringResource(Res.string.bookmarks_toggle_remove),
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            if (item.question.image > 0) {
                val imageRes = getImageResource(item.question.image)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onImageClick),
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
                text = item.question.question,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun BookmarksEmptyView(isFiltered: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                modifier = Modifier.size(64.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isFiltered) SearchX else Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(32.dp),
                    )
                }
            }

            Text(
                text = stringResource(
                    if (isFiltered) Res.string.catalog_empty_results else Res.string.bookmarks_empty_title,
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            if (!isFiltered) {
                Text(
                    text = stringResource(Res.string.bookmarks_empty_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.widthIn(max = 320.dp),
                )
            }
        }
    }
}
