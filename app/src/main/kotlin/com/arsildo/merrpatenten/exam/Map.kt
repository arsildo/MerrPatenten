package com.arsildo.merrpatenten.exam

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.R
import com.arsildo.merrpatenten.theme.Green
import com.arsildo.merrpatenten.theme.Red
import com.arsildo.merrpatenten.utils.ERRORS_ALLOWED
import com.arsildo.merrpatenten.utils.QUESTIONS_IN_EXAM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Map(
    sheetState: SheetState,
    isCompleted: Boolean,
    responses: List<String>,
    mistakes: List<Int>,
    errors: Int,
    onQuestionClicked: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        contentColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(top = 0, bottom = 0)
    ) {
        if (isCompleted) Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (errors > ERRORS_ALLOWED) Red.copy(.15f) else Green.copy(.15f),
                contentColor = if (errors > ERRORS_ALLOWED) Red else Green
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = if (errors > ERRORS_ALLOWED) R.string.failed else R.string.passed),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(text = "$errors Gabime", style = MaterialTheme.typography.titleLarge)
            }

        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(count = QUESTIONS_IN_EXAM) { page ->
                Question(
                    title = page,
                    containerColor = if (isCompleted) if (mistakes[page] == 0) Green else Red
                    else if (responses[page].isNotEmpty()) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (isCompleted) if (mistakes[page] == 0) Color.White else Color.White
                    else if (responses[page].isNotEmpty()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = { onQuestionClicked(page) }
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if (!isCompleted) Indicator(
                title = stringResource(id = R.string.completed_question),
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            )
            else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Indicator(
                        title = stringResource(id = R.string.false_checkbox),
                        containerColor = Red.copy(.15f),
                        contentColor = Red,
                    )
                    Indicator(
                        title = stringResource(id = R.string.true_checkbox),
                        containerColor = Green.copy(.15f),
                        contentColor = Green,
                    )

                }
            }
            IconButton(
                onClick = onDismissRequest,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Question(
    title: Int,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.inversePrimary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "${title + 1}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)

        )
    }
}

@Composable
private fun Indicator(
    title: String,
    containerColor: Color,
    contentColor: Color,
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(contentColor)
            )
        }
    }
}
