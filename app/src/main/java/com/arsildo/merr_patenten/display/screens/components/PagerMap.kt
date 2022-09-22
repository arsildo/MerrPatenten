package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PagerMap(
    isExamCompleted: MutableState<Boolean>,
    sheetState: ModalBottomSheetState,
    onPositionClicked: () -> Unit,
    onHideSheet: () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetElevation = 0.dp,
        sheetContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "${isExamCompleted.value}")

                LazyVerticalGrid(columns = GridCells.Fixed(count = 5)) {
                    items(40) {
                        MapItem(
                            position = "$it",
                            onPositionClick = onPositionClicked // TODO Pas Position
                        )
                    }
                }

                Button(onClick = onHideSheet) {
                    Text(text = "Hide Sheet")
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
    ) {
        Text(
            text = position,
            textAlign = TextAlign.Center,
        )
    }
}