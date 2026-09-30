package navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Controller for navigation events, modifying [NavigationState].
 *
 * Implements single-top tab navigation (Now in Android) and tab reselection
 * event emission (Navigation 3 Multiple Back Stacks Recipe).
 */
class Navigator(val state: NavigationState) {

    private val _reselectEvents = MutableSharedFlow<NavKey>(extraBufferCapacity = 1)
    val reselectEvents: SharedFlow<NavKey> = _reselectEvents.asSharedFlow()

    val backStack: List<NavKey>
        get() = state.backStack

    val currentKey: NavKey
        get() = state.currentKey

    val currentTab: NavKey?
        get() = state.currentTopLevelKey

    val canGoBack: Boolean
        get() = state.canGoBack

    /**
     * Navigate to a key.
     * - If key is the active top-level tab, triggers tab reselection.
     * - If key is another top-level tab, switches to that tab (single-top ordering).
     * - If key is a sub-destination or modal, adds to the current stack.
     */
    fun navigate(key: NavKey, popCurrent: Boolean = false) {
        when (key) {
            state.currentTopLevelKey -> onReselect(key)
            in state.topLevelKeys -> goToTopLevel(key, popCurrent)
            else -> goToKey(key, popCurrent)
        }
    }

    private fun onReselect(key: NavKey) {
        val subStack = state.subStacks[key]
        if (subStack != null && subStack.size > 1) {
            // Pop to root of tab
            while (subStack.size > 1) {
                subStack.removeLastOrNull()
            }
        } else {
            // Already at root of tab: emit reselect event (e.g. scroll to top)
            _reselectEvents.tryEmit(key)
        }
    }

    private fun goToTopLevel(key: NavKey, popCurrent: Boolean) {
        if (currentTab == null) {
            if (popCurrent) {
                state.rootStack.clear()
            }
            state.subStacks.forEach { (tabKey, stack) ->
                if (stack.isEmpty()) {
                    stack.add(tabKey)
                }
            }
        }
        state.topLevelStack.apply {
            if (key == state.primaryTopLevelKey) {
                // Exit through home: selecting primary tab resets visited tab history
                clear()
            } else {
                // Single-top tab re-ordering
                remove(key)
            }
            add(key)
        }
    }

    private fun goToKey(key: NavKey, popCurrent: Boolean) {
        if (currentTab != null) {
            val subStack = state.currentSubStack
            if (subStack != null) {
                if (popCurrent) {
                    subStack.removeLastOrNull()
                }
                subStack.remove(key)
                subStack.add(key)
            }
        } else {
            if (popCurrent) {
                state.rootStack.removeLastOrNull()
            }
            state.rootStack.remove(key)
            state.rootStack.add(key)
        }
    }

    /**
     * Pops top-most entry from the current substack or unwinds visited tabs.
     * Returns false if user is at root of primary tab or standalone root.
     */
    fun goBack(): Boolean {
        if (currentTab == null) {
            return if (state.rootStack.size > 1) {
                state.rootStack.removeLastOrNull()
                true
            } else {
                false
            }
        }

        val subStack = state.currentSubStack
        if (subStack != null && subStack.size > 1) {
            subStack.removeLastOrNull()
            return true
        }

        // At base of current tab: pop from topLevelStack if tab history exists
        if (state.topLevelStack.size > 1) {
            state.topLevelStack.removeLastOrNull()
            return true
        }

        // At root of primary tab: exit
        return false
    }

    /**
     * Force-pops root entry to trigger exit-the-app lifecycle.
     */
    fun popRoot(): Boolean = if (currentTab == null) {
        state.rootStack.removeLastOrNull() != null
    } else {
        val subStack = state.currentSubStack
        if (subStack != null && subStack.size > 1) {
            subStack.removeLastOrNull() != null
        } else {
            state.topLevelStack.removeLastOrNull() != null
        }
    }

    /**
     * Clears all tab substacks, resets visited tabs, and navigates to the given key.
     */
    fun clearAndNavigate(key: NavKey) {
        state.subStacks.values.forEach { it.clear() }
        state.topLevelStack.clear()
        state.rootStack.clear()

        if (key in state.topLevelKeys) {
            goToTopLevel(key, popCurrent = false)
        } else {
            state.rootStack.add(key)
        }
    }

    /**
     * Pops all entries in the given tab (or current tab) down to its root key.
     */
    fun popToRoot(tab: NavKey? = currentTab) {
        val targetTab = tab ?: state.primaryTopLevelKey
        val subStack = state.subStacks[targetTab]
        if (subStack != null && subStack.size > 1) {
            while (subStack.size > 1) {
                subStack.removeLastOrNull()
            }
        }
    }
}

/**
 * Creates and remembers a [Navigator] managing the [NavigationState].
 */
@Composable
fun rememberNavigator(startKey: NavKey, topLevelKeys: Set<NavKey>, configuration: SavedStateConfiguration): Navigator {
    val state = rememberNavigationState(
        startKey = startKey,
        topLevelKeys = topLevelKeys,
        configuration = configuration,
    )
    return remember(state) { Navigator(state) }
}
