package com.arsildo.merrpatenten.exam

import androidx.annotation.StringRes
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun EndExamButton(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    icon: ImageVector,
    containerColor: Color,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        text = { Text(text = stringResource(id = title)) },
        icon = {
            Icon(imageVector = icon, contentDescription = null)
        },
        containerColor = containerColor,
        contentColor = Color.White,
        elevation = FloatingActionButtonDefaults.loweredElevation(),
        shape = MaterialTheme.shapes.extraLarge,
        onClick = onClick,
        modifier = modifier,
    )

}