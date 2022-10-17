package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arsildo.merr_patenten.display.theme.GrayedOot
import com.arsildo.merr_patenten.display.theme.Green
import com.arsildo.merr_patenten.display.theme.Red
import com.arsildo.merr_patenten.display.theme.White

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PagerMap(
    isExamCompleted: Boolean,
    errors: Int,
    sheetState: ModalBottomSheetState,
    responseList: List<String>,
    mistakePositions: List<Int>,
    onPositionClicked: (Int) -> Unit,
    onHideSheet: () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colors.background,
        sheetContentColor = MaterialTheme.colors.onBackground,
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
                if (isExamCompleted) ConclusionTab(errors = errors)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(count = 5),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(40) {
                        MapItem(
                            position = "${it + 1}",
                            positionColor = if (!isExamCompleted) if (responseList[it].isEmpty()) MaterialTheme.colors.background else MaterialTheme.colors.primary
                            else if (mistakePositions[it] == 1) Red else Green,
                            positionTextColor = if (!isExamCompleted) if (responseList[it].isEmpty()) MaterialTheme.colors.onBackground else MaterialTheme.colors.onPrimary
                            else White,
                            borderColor = if (!isExamCompleted) if (responseList[it].isEmpty()) GrayedOot else MaterialTheme.colors.primary
                            else if (mistakePositions[it] == 1) Red else Green,
                            onPositionClick = { onPositionClicked(it) }
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
                        Indicator(title = "Plotesuar", color = MaterialTheme.colors.primary)
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Indicator(title = "Gabim", color = Red)
                            Indicator(title = "Saktë", color = Green)

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
private fun MapItem(
    position: String,
    positionColor: Color,
    positionTextColor: Color,
    borderColor: Color,
    onPositionClick: () -> Unit,
) {
    Card(
        onClick = onPositionClick,
        backgroundColor = positionColor,
        contentColor = positionTextColor,
        border = BorderStroke(1.dp, borderColor),
        shape = MaterialTheme.shapes.small,
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

@Composable
private fun ConclusionTab(errors: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$errors Gabime",
            color = if (errors > 4) Red else Green,
            fontSize = 25.sp
        )
        Text(
            text = if (errors > 4) "Ju Humbet!" else "Ju Fituat!",
            fontSize = 25.sp,
            color = if (errors > 4) Red else Green
        )

    }
}

@Composable
fun Indicator(
    title: String,
    color: Color,
) {
    Row(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.small)
            .background(color.copy(.2f))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = color, fontSize = 13.sp)
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(color)
        )
    }
}