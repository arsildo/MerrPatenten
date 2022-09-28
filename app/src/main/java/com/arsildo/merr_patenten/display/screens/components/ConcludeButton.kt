package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConcludeButton(
    isExamCompleted: Boolean,
    onClick: () -> Unit,
) {
    var confirm by remember { mutableStateOf(2) }
    Button(
        onClick = {
            if (isExamCompleted) onClick() else confirm--; if (confirm == 0) onClick()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (!isExamCompleted) MaterialTheme.colors.error else MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            if (!isExamCompleted) Icons.Rounded.DoneAll else Icons.Rounded.Map,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = if (!isExamCompleted) "Perfundo Provimin ($confirm)" else "Shiko Gabimet",
        )
    }

}