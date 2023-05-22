package com.arsildo.merrpatenten.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.R

@Composable
fun BoxScope.ResultStoringDisabled(
    enabled: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(.7f)
            .align(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.sun_rain),
            contentDescription = null,
        )
        Text(
            text = if (enabled) stringResource(id = R.string.results_empty)
            else stringResource(id = R.string.results_storing_disabled),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}