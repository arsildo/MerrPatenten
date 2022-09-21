package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PagerMap(
    sheetState: ModalBottomSheetState,
    onHideSheet: () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            Column(Modifier.fillMaxWidth()) {
                Text(text = "Bottom Sheet")

                LazyVerticalGrid(columns = GridCells.Fixed(count = 2)) {
                    items(10){
                        Text(text = "$it")
                    }
                }

                Button(onClick = onHideSheet) {
                    Text(text = "Hide Sheet")
                }
            }
        }
    ) {}
}