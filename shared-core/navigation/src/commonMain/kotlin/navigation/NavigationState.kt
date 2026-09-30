package navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.savedstate.serialization.SavedStateConfiguration

/**
 * Multi-stack navigation state supporting standalone roots,
 * top-level dashboard tabs with substacks, and isolated per-stack decorators.
 *
 * Combines Google Navigation 3 Multiple Back Stacks Recipe and Now in Android patterns.
 *
 * @param startKey Initial destination key.
 * @param topLevelKeys Top-level tabs in the dashboard. First key acts as primary tab.
 * @param topLevelStack Backstack of visited top-level keys.
 * @param subStacks Map of backstacks for each top-level key.
 * @param rootStack Backstack for standalone or full-screen flows.
 */
class NavigationState(
    val startKey: NavKey,
    val topLevelKeys: Set<NavKey>,
    val topLevelStack: NavBackStack<NavKey>,
    val subStacks: Map<NavKey, NavBackStack<NavKey>>,
    val rootStack: NavBackStack<NavKey>,
) {
    val primaryTopLevelKey: NavKey
        get() = topLevelKeys.firstOrNull() ?: startKey

    val currentTopLevelKey: NavKey? by derivedStateOf {
        topLevelStack.lastOrNull()
    }

    val currentSubStack: NavBackStack<NavKey>?
        get() = currentTopLevelKey?.let { subStacks[it] }

    val currentKey: NavKey by derivedStateOf {
        if (currentTopLevelKey != null) {
            currentSubStack?.lastOrNull() ?: primaryTopLevelKey
        } else {
            rootStack.lastOrNull() ?: startKey
        }
    }

    val backStack: List<NavKey> by derivedStateOf {
        if (currentTopLevelKey != null) {
            topLevelStack.flatMap { subStacks[it] ?: emptyList() }
        } else {
            rootStack.toList()
        }
    }

    val canGoBack: Boolean by derivedStateOf {
        if (currentTopLevelKey == null) {
            rootStack.size > 1
        } else {
            val currentStack = currentSubStack
            val hasSubStackHistory = currentStack != null && currentStack.size > 1
            val hasTabHistory = topLevelStack.size > 1
            hasSubStackHistory || hasTabHistory
        }
    }
}

/**
 * Creates and remembers a [NavigationState] across process death and configuration changes.
 */
@Composable
fun rememberNavigationState(startKey: NavKey, topLevelKeys: Set<NavKey>, configuration: SavedStateConfiguration): NavigationState {
    val isStartInTopLevel = startKey in topLevelKeys

    val rootStack = if (isStartInTopLevel) {
        rememberNavBackStack(configuration)
    } else {
        rememberNavBackStack(configuration, startKey)
    }

    val topLevelStack = if (isStartInTopLevel) {
        rememberNavBackStack(configuration, startKey)
    } else {
        rememberNavBackStack(configuration)
    }

    val subStacks = topLevelKeys.associateWith { key ->
        rememberNavBackStack(configuration, key)
    }

    return remember(startKey, topLevelKeys, configuration) {
        NavigationState(
            startKey = startKey,
            topLevelKeys = topLevelKeys,
            topLevelStack = topLevelStack,
            subStacks = subStacks,
            rootStack = rootStack,
        )
    }
}

/**
 * Converts [NavigationState] into decorated [NavEntry]s.
 * Each substack and the root stack maintain their own isolated decorators.
 */
@Composable
fun NavigationState.toEntries(entryProvider: (NavKey) -> NavEntry<NavKey>): SnapshotStateList<NavEntry<NavKey>> {
    val rootDecoratedEntries = rememberDecoratedNavEntries(
        backStack = rootStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberSharedViewModelStoreNavEntryDecorator<NavKey>(),
        ),
        entryProvider = entryProvider,
    )

    val subStackDecoratedEntries = subStacks.mapValues { (_, stack) ->
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
                rememberSharedViewModelStoreNavEntryDecorator<NavKey>(),
            ),
            entryProvider = entryProvider,
        )
    }

    return if (currentTopLevelKey != null) {
        topLevelStack
            .flatMap { subStackDecoratedEntries[it] ?: emptyList() }
            .toMutableStateList()
    } else {
        rootDecoratedEntries.toMutableStateList()
    }
}
