package com.arsildo.merrpatenten.shared.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arsildo.merrpatenten.shared.core.designsystem.Red
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.end_exam
import merrpatenten.shared_core.design_system.generated.resources.exit_exam
import merrpatenten.shared_core.design_system.generated.resources.restart_exam
import org.jetbrains.compose.resources.stringResource

@Composable
fun EndExamButton(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.end_exam),
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ActionButton(
        title = title,
        icon = icon,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}

@Composable
fun RestartExamButton(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.restart_exam),
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ActionButton(
        title = title,
        icon = icon,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = modifier
    )
}

@Composable
fun ExitExamButton(
    modifier: Modifier = Modifier,
    title: String = stringResource(Res.string.exit_exam),
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ActionButton(
        title = title,
        icon = icon,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Red,
            contentColor = Color.White
        ),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    colors: ButtonColors,
) {
    Button(
        onClick = onClick,
        shapes = ButtonShapes(
            shape = MaterialTheme.shapes.large,
            pressedShape = MaterialTheme.shapes.small
        ),
        contentPadding = PaddingValues(16.dp),
        colors = colors,
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
