package com.arsildo.merrpatenten.shared.feature.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.arsildo.merrpatenten.shared.core.designsystem.ERRORS_ALLOWED
import com.arsildo.merrpatenten.shared.core.model.ExamResult
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.false_checkbox
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResultList(
    results: List<ExamResult>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(results) { result ->
            ResultItem(result = result)
        }
    }
}

@Composable
private fun ResultItem(
    result: ExamResult,
) {
    val errors = result.errors
    Card(
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (errors > ERRORS_ALLOWED)
                MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primary,
            contentColor = if (errors > ERRORS_ALLOWED)
                MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimary
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
                    text = "$errors" + if (errors != 1) " ${stringResource(Res.string.false_checkbox)}e"
                    else " ${stringResource(Res.string.false_checkbox)}",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(text = "${result.time} min", style = MaterialTheme.typography.labelMedium)
            }
            Icon(
                imageVector = if (errors > ERRORS_ALLOWED) Icons.Rounded.Clear else Icons.Rounded.CheckCircleOutline,
                contentDescription = null
            )
        }
    }
}
