package com.arsildo.merr_patenten.display.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.arsildo.merr_patenten.display.screens.components.ScreenLayout
import com.arsildo.merr_patenten.logic.cache.UserPreferences
import com.arsildo.merr_patenten.logic.navigation.Destinations
import kotlinx.coroutines.launch

@Composable
fun PreferencesScreen(navController: NavController) {

    val dataStore = UserPreferences(LocalContext.current)
    val themePreference = dataStore.getThemePreference.collectAsState(initial = "light_mode").value
    val scope = rememberCoroutineScope()

    ScreenLayout {
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { navController.navigate(Destinations.Main.route) },
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
                text = "Preferencat",
                color = MaterialTheme.colors.primary,
                fontSize = 28.sp
            )
        }

        SettingItem(
            title = "Night Mode",
            icon = Icons.Outlined.DarkMode,
            checked = themePreference == "dark_mode",
            onCheckedChange = {
                if (themePreference == "light_mode") {
                    scope.launch {
                        dataStore.setThemePreference("dark_mode")
                    }
                } else scope.launch { dataStore.setThemePreference("light_mode") }
            }
        )
        SettingItem(
            title = "Kujto rezultatet",
            icon = Icons.Outlined.QueryStats,
            checked = false,
            onCheckedChange = { }
        )

    }
}

@Composable
fun SettingItem(
    title: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.primary.copy(.1f))
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(end = 9.dp)
                    .size(28.dp)

            )
            Text(
                text = title,
                color = MaterialTheme.colors.primary,
                fontSize = 18.sp
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = { onCheckedChange(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                checkedTrackColor = MaterialTheme.colors.primary,
                uncheckedThumbColor = MaterialTheme.colors.primary,
                uncheckedTrackColor = MaterialTheme.colors.primary // todo adjust
            )
        )
    }
}

