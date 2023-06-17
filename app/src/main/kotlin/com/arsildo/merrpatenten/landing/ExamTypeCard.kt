package com.arsildo.merrpatenten.landing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamTypeCard(onClick: () -> Unit) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        shape = MaterialTheme.shapes.extraLarge,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.questionnaire_category_one),
                style = MaterialTheme.typography.titleMedium,
            )
            Icon(
                painter = painterResource(id = R.drawable.ico_category),
                contentDescription = null,
                modifier = Modifier.aspectRatio(2.25f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Text(text = "40 MINUTA", style = MaterialTheme.typography.labelMedium)
                Text(text = "40 PYETJE", style = MaterialTheme.typography.labelMedium)
                Text(text = "4 GABIME", style = MaterialTheme.typography.labelMedium)
            }
            Button(
                onClick = onClick,
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) { Text(text = stringResource(id = R.string.start_exam)) }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ExamChip(
    text: String
) {
    Chip(
        content = {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        },
        colors = ChipDefaults.chipColors(backgroundColor = MaterialTheme.colorScheme.secondaryContainer),
        border = BorderStroke(
            width = 1.dp,
            MaterialTheme.colorScheme.inverseSurface
        ),
        onClick = { /*TODO*/ }
    )
}
