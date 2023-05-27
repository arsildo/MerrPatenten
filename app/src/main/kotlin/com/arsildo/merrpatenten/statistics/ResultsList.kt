package com.arsildo.merrpatenten.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.theme.Green
import com.arsildo.merrpatenten.theme.Red
import com.arsildo.merrpatenten.utils.ERRORS_ALLOWED

@Composable
fun ResultList(
    results: List<Results>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(results) {
            Result(results = it)
        }
    }
}

@Composable
private fun Result(
    results: Results,
) {
    val errors = results.errors
    Card(
        colors = CardDefaults.elevatedCardColors(
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            containerColor = if (errors > ERRORS_ALLOWED) Red.copy(.4f) else Green.copy(.4f)
        ),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "$errors" + if (errors != 1) " Gabime" else " Gabim",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = results.time + " min", style = MaterialTheme.typography.titleSmall)
            }
            Icon(
                imageVector = if (errors > 4) Icons.Rounded.Clear else Icons.Rounded.CheckCircleOutline,
                contentDescription = null
            )
        }
    }
}
