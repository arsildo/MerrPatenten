package com.arsildo.merr_patenten.display.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConcludeButton(
    isExamCompleted: MutableState<Boolean>,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick, colors = ButtonDefaults.buttonColors(
            backgroundColor = if (!isExamCompleted.value) MaterialTheme.colors.error else MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.elevation(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            if (!isExamCompleted.value) Icons.Rounded.DoneAll else Icons.Rounded.ExpandLess,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = if (!isExamCompleted.value) "Perfundo Provimin" else "Shiko Gabimet")
    }

}