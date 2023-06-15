package com.arsildo.merrpatenten.landing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun ExamTypeCard(
    onClick: () -> Unit,
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "A1 A2 B1 B",
                style = MaterialTheme.typography.headlineMedium
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ico_category),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp)
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    Chip(
                        content = {
                            Text(
                                text = "40 Minuta",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        colors = ChipDefaults.outlinedChipColors(),
                        onClick = { /*TODO*/ }
                    )
                    Chip(
                        content = {
                            Text(
                                text = "40 Pyetje",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        colors = ChipDefaults.outlinedChipColors(),
                        onClick = { /*TODO*/ }
                    )
                    Chip(
                        content = {
                            Text(
                                text = "4 Gabime",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        colors = ChipDefaults.outlinedChipColors(),
                        onClick = { /*TODO*/ }
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy((4 + 2).dp)
            ) {
                Button(
                    onClick = onClick,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.start_exam))
                }
                Text(
                    text = stringResource(id = R.string.questionnaire_version),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 8.dp, top = 4.dp)
                )
            }
        }
    }
}
