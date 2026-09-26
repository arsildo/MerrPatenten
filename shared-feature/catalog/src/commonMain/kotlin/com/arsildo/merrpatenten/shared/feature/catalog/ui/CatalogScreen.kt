package com.arsildo.merrpatenten.shared.feature.catalog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.rememberWindowAdaptiveInfo
import com.arsildo.merrpatenten.shared.core.model.Question
import com.arsildo.merrpatenten.shared.core.model.RoadSign
import com.arsildo.merrpatenten.shared.core.model.SignCategory
import com.arsildo.merrpatenten.shared.feature.catalog.CatalogUiState
import com.arsildo.merrpatenten.shared.feature.catalog.CatalogViewModel
import com.arsildo.merrpatenten.shared.feature.catalog.ui.components.RoadSignCard
import kotlinx.coroutines.flow.Flow
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CatalogRoute(
    onBackPress: () -> Unit,
    onSignClick: (id: Int) -> Unit,
    onImageDetailsClick: (index: Int) -> Unit = {},
    viewModel: CatalogViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CatalogScreen(
        uiState = uiState,
        onBackPress = onBackPress,
        onSignClick = onSignClick,
        onImageDetailsClick = onImageDetailsClick,
        onCategorySelected = viewModel::onCategorySelected,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onToggleSearch = viewModel::toggleSearch,
        onGetQuestionsForSign = viewModel::getQuestionsForSign,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    uiState: CatalogUiState,
    onBackPress: () -> Unit,
    onSignClick: (id: Int) -> Unit,
    onCategorySelected: (category: SignCategory) -> Unit,
    onSearchQueryChanged: (query: String) -> Unit,
    onToggleSearch: (isSearching: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onImageDetailsClick: (index: Int) -> Unit = {},
    onGetQuestionsForSign: ((imageId: Int) -> Flow<List<Question>>)? = null,
) {
    val hapticFeedback = LocalHapticFeedback.current

    Scaffold(
        contentWindowInsets = WindowInsets(bottom = 0),
        topBar = {
            TopAppBar(
                title = {
                    if (uiState.isSearching) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchQueryChanged,
                            placeholder = {
                                Text(
                                    text = stringResource(Res.string.catalog_search_placeholder),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            ),
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { onSearchQueryChanged("") }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = stringResource(Res.string.close),
                                        )
                                    }
                                }
                            },
                        )
                    } else {
                        Text(
                            text = stringResource(Res.string.catalog_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (uiState.isSearching) {
                                onToggleSearch(false)
                            } else {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onBackPress()
                            }
                        },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                        )
                    }
                },
                actions = {
                    if (!uiState.isSearching) {
                        IconButton(
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onToggleSearch(true)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = stringResource(Res.string.catalog_search_placeholder),
                            )
                        }
                    }
                },
            )
        },
        modifier = modifier,
    ) { contentPadding ->
        val adaptiveInfo = rememberWindowAdaptiveInfo()
        val isDualPane = adaptiveInfo.isDualPane

        var selectedSignId by rememberSaveable { mutableStateOf<Int?>(null) }
        val selectedSign = uiState.items.find { it.id == selectedSignId } ?: uiState.items.firstOrNull()

        if (isDualPane) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = contentPadding.calculateTopPadding()),
            ) {
                CatalogListPane(
                    uiState = uiState,
                    selectedSignId = selectedSign?.id,
                    onCategorySelected = onCategorySelected,
                    onSignClick = { id ->
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        selectedSignId = id
                    },
                    contentPadding = PaddingValues(
                        bottom = contentPadding.calculateBottomPadding() + 24.dp,
                    ),
                    modifier = Modifier
                        .weight(0.42f)
                        .fillMaxHeight(),
                    adaptiveMinSize = 130.dp,
                )

                VerticalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                )

                Surface(
                    modifier = Modifier
                        .weight(0.58f)
                        .fillMaxHeight(),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    if (selectedSign != null) {
                        val relatedQuestions by produceState(
                            initialValue = emptyList<Question>(),
                            key1 = selectedSign.imageResNumber,
                        ) {
                            if (onGetQuestionsForSign != null) {
                                onGetQuestionsForSign(selectedSign.imageResNumber).collect {
                                    value = it
                                }
                            } else {
                                value = emptyList()
                            }
                        }
                        SignDetailBody(
                            sign = selectedSign,
                            relatedQuestions = relatedQuestions,
                            onImageDetailsClick = onImageDetailsClick,
                            modifier = Modifier.padding(
                                bottom = contentPadding.calculateBottomPadding(),
                            ),
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(Res.string.catalog_empty_results),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        } else {
            CatalogListPane(
                uiState = uiState,
                selectedSignId = null,
                onCategorySelected = onCategorySelected,
                onSignClick = { id ->
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onSignClick(id)
                },
                contentPadding = PaddingValues(
                    top = contentPadding.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding() + 24.dp,
                ),
                modifier = Modifier.fillMaxSize(),
                adaptiveMinSize = 160.dp,
            )
        }
    }
}

@Composable
private fun CatalogListPane(
    uiState: CatalogUiState,
    selectedSignId: Int?,
    onCategorySelected: (category: SignCategory) -> Unit,
    onSignClick: (id: Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    adaptiveMinSize: androidx.compose.ui.unit.Dp = 160.dp,
) {
    val hapticFeedback = LocalHapticFeedback.current
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(top = contentPadding.calculateTopPadding()),
    ) {
        // Category Filter Chips
        val categories = listOf(
            SignCategory.ALL to Res.string.catalog_category_all,
            SignCategory.WARNING to Res.string.catalog_category_warning,
            SignCategory.PROHIBITORY to Res.string.catalog_category_prohibitory,
            SignCategory.INFORMATIVE to Res.string.catalog_category_informative,
            SignCategory.ROAD_MARKINGS to Res.string.catalog_category_markings,
            SignCategory.POLICE_SIGNALS to Res.string.catalog_category_police,
            SignCategory.INTERSECTIONS to Res.string.catalog_category_intersections,
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            items(categories) { (category, stringRes) ->
                val selected = uiState.selectedCategory == category
                FilterChip(
                    selected = selected,
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onCategorySelected(category)
                    },
                    label = {
                        Text(
                            text = stringResource(stringRes),
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
            }
        }

        // Results Grid
        if (uiState.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = contentPadding.calculateBottomPadding()),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.outline,
                    )
                    Text(
                        text = stringResource(Res.string.catalog_empty_results),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = adaptiveMinSize),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = contentPadding.calculateBottomPadding(),
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(uiState.items, key = { it.id }) { sign ->
                    RoadSignCard(
                        sign = sign,
                        selected = sign.id == selectedSignId,
                        onClick = { onSignClick(sign.id) },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CatalogScreenPreview() {
    MerrPatentenTheme {
        CatalogScreen(
            uiState = CatalogUiState(
                items = listOf(
                    RoadSign(
                        id = 1,
                        imageResNumber = 1,
                        title = "Rrugë e deformuar",
                        category = SignCategory.WARNING,
                        code = "Fig. 1",
                        description = "Paralajmëron një pjesë rruge të deformuar, me gropa apo me sipërfaqe të dëmtuar.",
                    ),
                ),
            ),
            onBackPress = {},
            onSignClick = {},
            onCategorySelected = {},
            onSearchQueryChanged = {},
            onToggleSearch = {},
        )
    }
}
