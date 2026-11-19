package com.arsildo.merrpatenten.shared.feature.catalog.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.arsildo.merrpatenten.shared.core.designsystem.components.EmptyStateTone
import com.arsildo.merrpatenten.shared.core.designsystem.components.ExpressiveEmptyState
import com.arsildo.merrpatenten.shared.core.designsystem.icons.BookOpen
import com.arsildo.merrpatenten.shared.feature.catalog.ui.CatalogRoute
import com.arsildo.merrpatenten.shared.feature.catalog.ui.SignDetailRoute
import kotlinx.serialization.Serializable
import merrpatenten.shared_core.design_system.generated.resources.*
import navigation.ListDetailSceneStrategy
import org.jetbrains.compose.resources.stringResource

@Serializable
object Catalog : NavKey

@Serializable
data class SignDetail(val signId: Int) : NavKey

fun EntryProviderScope<NavKey>.catalogEntry(
    backStack: NavBackStack<NavKey>,
    onBackPress: () -> Unit = { backStack.removeLastOrNull() },
    onImageDetailsClick: (index: Int) -> Unit = {},
) {
    entry<Catalog>(
        metadata = ListDetailSceneStrategy.listPane(
            detailPlaceholder = {
                ExpressiveEmptyState(
                    icon = BookOpen,
                    title = stringResource(Res.string.catalog_empty_results),
                    tone = EmptyStateTone.Default,
                    modifier = Modifier.fillMaxSize(),
                )
            },
        ),
    ) {
        val selectedSignId = (backStack.lastOrNull() as? SignDetail)?.signId
        CatalogRoute(
            selectedSignId = selectedSignId,
            onSignClick = { signId ->
                if (backStack.lastOrNull() is SignDetail) {
                    backStack.removeLastOrNull()
                }
                backStack.add(SignDetail(signId))
            },
        )
    }

    entry<SignDetail>(
        metadata = ListDetailSceneStrategy.detailPane(),
    ) { key ->
        SignDetailRoute(
            signId = key.signId,
            onBackPress = onBackPress,
            onImageDetailsClick = onImageDetailsClick,
        )
    }
}
