package com.arsildo.merrpatenten.shared.feature.catalog.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arsildo.merrpatenten.shared.core.designsystem.MerrPatentenTheme
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.rememberStartToolbarClearance
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.rememberWindowAdaptiveInfo
import com.arsildo.merrpatenten.shared.core.designsystem.adaptive.startPadding
import com.arsildo.merrpatenten.shared.core.designsystem.components.EmptyStateTone
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExpressiveEmptyState
import com.arsildo.merrpatenten.shared.core.designsystem.icons.ArrowLeft
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Search
import com.arsildo.merrpatenten.shared.core.designsystem.icons.SearchX
import com.arsildo.merrpatenten.shared.core.designsystem.icons.X
import com.arsildo.merrpatenten.shared.core.model.RoadSign
import com.arsildo.merrpatenten.shared.core.model.SignCategory
import com.arsildo.merrpatenten.shared.feature.catalog.CatalogUiState
import com.arsildo.merrpatenten.shared.feature.catalog.CatalogViewModel
import com.arsildo.merrpatenten.shared.feature.catalog.ui.components.RoadSignCard
import merrpatenten.shared_core.design_system.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CatalogRoute(viewModel: CatalogViewModel = koinViewModel(), onSignClick: (id: Int) -> Unit, selectedSignId: Int? = null) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CatalogScreen(
        uiState = uiState,
        selectedSignId = selectedSignId,
        onSignClick = onSignClick,
        onCategorySelected = viewModel::onCategorySelected,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onToggleSearch = viewModel::toggleSearch,
    )
}

@Composable
private fun CatalogScreen(
    modifier: Modifier = Modifier,
    uiState: CatalogUiState,
    onSignClick: (id: Int) -> Unit,
    onCategorySelected: (category: SignCategory) -> Unit,
    onSearchQueryChanged: (query: String) -> Unit,
    onToggleSearch: (isSearching: Boolean) -> Unit,
    selectedSignId: Int? = null,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val adaptiveInfo = rememberWindowAdaptiveInfo()
    val startToolbarClearance = rememberStartToolbarClearance(adaptiveInfo.isCompact)
    val bottomToolbarClearance = if (adaptiveInfo.isCompact) 96.dp else 24.dp
    val windowInsets = if (adaptiveInfo.isDualPane) {
        WindowInsets.safeDrawing.only(WindowInsetsSides.Start + WindowInsetsSides.Top)
    } else {
        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
    }

    Scaffold(
        modifier = modifier.startPadding { startToolbarClearance.value },
        contentWindowInsets = windowInsets,
        topBar = {
            TopAppBar(
                windowInsets = windowInsets,
                title = {
                    if (uiState.isSearching) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchQueryChanged,
                            textStyle = MaterialTheme.typography.bodyMedium,
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
                                    IconButton(
                                        onClick = { onSearchQueryChanged("") },
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
                    if (uiState.isSearching) {
                        IconButton(
                            onClick = { onToggleSearch(false) },
                        ) {
                            Icon(
                                imageVector = ArrowLeft,
                                contentDescription = stringResource(Res.string.back),
                            )
                        }
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
                                imageVector = Search,
                                contentDescription = stringResource(Res.string.catalog_search_placeholder),
                            )
                        }
                    }
                },
            )
        },
    ) { contentPadding ->
        CatalogListPane(
            uiState = uiState,
            selectedSignId = selectedSignId,
            onCategorySelected = onCategorySelected,
            onSignClick = { id ->
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onSignClick(id)
            },
            contentPadding = PaddingValues(
                bottom = contentPadding.calculateBottomPadding() +
                    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + bottomToolbarClearance,
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding),
        )
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
) {
    val hapticFeedback = LocalHapticFeedback.current
    Column(
        modifier = modifier.fillMaxHeight(),
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
            items(categories) { [category, stringRes] ->
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
                )
            }
        }

        // Results Grid
        if (uiState.items.isEmpty()) {
            ExpressiveEmptyState(
                icon = SearchX,
                title = stringResource(Res.string.catalog_empty_results),
                tone = EmptyStateTone.Search,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = contentPadding.calculateBottomPadding()),
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
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
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CatalogScreenPreview() = MerrPatentenTheme {
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
        onSignClick = {},
        onCategorySelected = {},
        onSearchQueryChanged = {},
        onToggleSearch = {},
    )
}
