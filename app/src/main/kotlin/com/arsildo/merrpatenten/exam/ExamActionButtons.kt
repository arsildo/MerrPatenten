package com.arsildo.merrpatenten.exam

import androidx.annotation.StringRes
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun EndExamButton(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        text = { Text(text = stringResource(id = title)) },
        icon = {
            Icon(imageVector = icon, contentDescription = null)
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        elevation = FloatingActionButtonDefaults.loweredElevation(),
        shape = MaterialTheme.shapes.extraLarge,
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
fun RestartExamButton(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        text = { Text(text = stringResource(id = title)) },
        icon = {
            Icon(imageVector = icon, contentDescription = null)
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        elevation = FloatingActionButtonDefaults.loweredElevation(),
        shape = MaterialTheme.shapes.extraLarge,
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
fun ExitExamButton(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        text = { Text(text = stringResource(id = title)) },
        icon = {
            Icon(imageVector = icon, contentDescription = null)
        },
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        elevation = FloatingActionButtonDefaults.loweredElevation(),
        shape = MaterialTheme.shapes.extraLarge,
        onClick = onClick,
        modifier = modifier,
    )
}