package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.HorizontalRule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PagerMap(
    isExamCompleted: Boolean,
    sheetState: ModalBottomSheetState,
    onPositionClicked: (Int) -> Unit,
    onHideSheet: () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colors.background,
        scrimColor = MaterialTheme.colors.background.copy(.9f),
        sheetElevation = 8.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.HorizontalRule,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (isExamCompleted) {
                    Row() {
                        Text(text = "Rezultati")
                    }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(count = 5),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(40) {
                        MapItem(
                            position = "${it + 1}",
                            onPositionClick = { onPositionClicked(it) } // TODO Pas Position
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isExamCompleted) {
                        Text(text = "Indicator")
                    } else {
                        Row {
                            Text(text = "Indicator")
                            Text(text = "Indicator")
                        }
                    }

                    IconButton(onClick = onHideSheet) {
                        Icon(
                            Icons.Rounded.ExpandMore,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                        )
                    }
                }

            }
        }
    ) {}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapItem(
    position: String,
    onPositionClick: () -> Unit,
) {
    Card(
        onClick = onPositionClick,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.primary,
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        shape = MaterialTheme.shapes.medium,
        elevation = 0.dp,
    ) {
        Text(
            text = position,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        )
    }
}