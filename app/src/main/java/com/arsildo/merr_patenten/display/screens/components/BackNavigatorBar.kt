package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BackNavigatorBar(
    title:String,
    onNavigateBackDestination: () -> Unit,
    trailingIcon: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            IconButton(
                onClick = onNavigateBackDestination,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(32.dp)
                    .padding(2.dp)
            ) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = title,
                color = MaterialTheme.colors.primary,
                fontSize = 28.sp
            )

        }
        trailingIcon()
    }
}