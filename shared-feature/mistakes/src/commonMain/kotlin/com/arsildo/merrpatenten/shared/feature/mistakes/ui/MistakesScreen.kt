package com.arsildo.merrpatenten.shared.feature.mistakes.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
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
import com.arsildo.merrpatenten.shared.core.designsystem.getImageResource
import com.arsildo.merrpatenten.shared.core.designsystem.semanticColors
import com.arsildo.merrpatenten.shared.core.model.MistakeQuestion
import com.arsildo.merrpatenten.shared.feature.mistakes.MistakesUiState
import com.arsildo.merrpatenten.shared.feature.mistakes.MistakesViewModel
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MistakesRoute(
    onBackPress: () -> Unit,
    onStartPractice: (category: String) -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    viewModel: MistakesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MistakesScreen(
        uiState = uiState,
        onBackPress = onBackPress,
        onStartPractice = { onStartPractice(uiState.selectedCategory) },
        onImageDetailsClick = onImageDetailsClick,
        onSelectCategory = viewModel::onSelectCategory,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onDeleteMistake = viewModel::deleteMistake,
        onClearCategory = viewModel::clearCategoryMistakes,
    )
}

@Composable
fun MistakesScreen(
    uiState: MistakesUiState,
    onBackPress: () -> Unit,
    onStartPractice: () -> Unit,
    onImageDetailsClick: (index: Int) -> Unit,
    onSelectCategory: (category: String) -> Unit,
    onSearchQueryChange: (query: String) -> Unit,
    onDeleteMistake: (questionId: Int, category: String) -> Unit,
    onClearCategory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hapticFeedback = LocalHapticFeedback.current
    var showClearDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.mistakes_title),
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
                actions = {
                    if (uiState.totalCount > 0) {
                        IconButton(
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                showClearDialog = true
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DeleteSweep,
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
                        onStartPractice()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
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
                    modifier = Modifier.navigationBarsPadding(),
                    elevation = FloatingActionButtonDefaults.loweredElevation(),
                )
            }
        },
        modifier = modifier,
        contentWindowInsets = WindowInsets(bottom = 0),
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
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
                placeholder = {
                    Text(
                        text = stringResource(Res.string.catalog_search_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = stringResource(Res.string.close),
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
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 340.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 96.dp,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(
                        items = uiState.mistakes,
                        key = { "${it.question.id}_${it.question.category}" },
                    ) { item ->
                        MistakeQuestionCard(
                            item = item,
                            onImageClick = { onImageDetailsClick(item.question.image) },
                            onDeleteClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onDeleteMistake(item.question.id, item.question.category)
                            },
                        )
                    }
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text(stringResource(Res.string.mistakes_clear_category)) },
            text = { Text(stringResource(Res.string.mistakes_clear_category_desc)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onClearCategory()
                        showClearDialog = false
                    },
                ) {
                    Text(
                        text = stringResource(Res.string.results_confirm),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text(stringResource(Res.string.results_dismiss))
                }
            },
        )
    }
}

@Composable
private fun CategoryTabs(selectedCategory: String, onCategorySelected: (category: String) -> Unit, modifier: Modifier = Modifier) {
    val categories = listOf(
        "B" to stringResource(Res.string.questionnaire_category_one),
        "C" to stringResource(Res.string.questionnaire_category_two),
        "D" to stringResource(Res.string.questionnaire_category_three),
    )

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        categories.forEachIndexed { index, (catKey, catLabel) ->
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

@Composable
private fun MistakeQuestionCard(
    item: MistakeQuestion,
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
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(Res.string.mistakes_delete_item),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // Image Thumbnail (if present)
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
                        imageVector = if (isTrue) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                        contentDescription = null,
                        tint = if (isTrue) MaterialTheme.semanticColors.success else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = stringResource(Res.string.correct_answer_feedback) + ": ${item.question.answer}",
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
    Box(
        modifier = modifier.padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                imageVector = if (isFiltered) Icons.Rounded.SearchOff else Icons.Rounded.CheckCircleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(72.dp),
            )
            Text(
                text = if (isFiltered) {
                    stringResource(Res.string.catalog_empty_results)
                } else {
                    stringResource(Res.string.mistakes_empty_title)
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = if (isFiltered) "" else stringResource(Res.string.mistakes_empty_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}
