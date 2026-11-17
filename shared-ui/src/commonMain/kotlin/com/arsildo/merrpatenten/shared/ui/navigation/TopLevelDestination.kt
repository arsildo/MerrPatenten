package com.arsildo.merrpatenten.shared.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.core.designsystem.icons.BookBookmark
import com.arsildo.merrpatenten.shared.core.designsystem.icons.BookOpen
import com.arsildo.merrpatenten.shared.core.designsystem.icons.GraphStar
import com.arsildo.merrpatenten.shared.core.designsystem.icons.House
import com.arsildo.merrpatenten.shared.core.designsystem.icons.Settings
import com.arsildo.merrpatenten.shared.feature.catalog.navigation.Catalog
import com.arsildo.merrpatenten.shared.feature.dashboard.navigation.Dashboard
import com.arsildo.merrpatenten.shared.feature.mistakes.navigation.Mistakes
import com.arsildo.merrpatenten.shared.feature.preferences.navigation.Preferences
import com.arsildo.merrpatenten.shared.feature.statistics.navigation.Statistics
import merrpatenten.shared_core.design_system.generated.resources.Res
import merrpatenten.shared_core.design_system.generated.resources.nav_catalog
import merrpatenten.shared_core.design_system.generated.resources.nav_home
import merrpatenten.shared_core.design_system.generated.resources.nav_mistakes
import merrpatenten.shared_core.design_system.generated.resources.nav_preferences
import merrpatenten.shared_core.design_system.generated.resources.nav_statistics
import org.jetbrains.compose.resources.StringResource

internal data class TopLevelDestination(val key: NavKey, val icon: ImageVector, val labelRes: StringResource)

internal val topLevelDestinations = listOf(
    TopLevelDestination(
        key = Dashboard,
        icon = House,
        labelRes = Res.string.nav_home,
    ),
    TopLevelDestination(
        key = Catalog,
        icon = BookOpen,
        labelRes = Res.string.nav_catalog,
    ),
    TopLevelDestination(
        key = Mistakes,
        icon = BookBookmark,
        labelRes = Res.string.nav_mistakes,
    ),
    TopLevelDestination(
        key = Statistics,
        icon = GraphStar,
        labelRes = Res.string.nav_statistics,
    ),
    TopLevelDestination(
        key = Preferences,
        icon = Settings,
        labelRes = Res.string.nav_preferences,
    ),
)
