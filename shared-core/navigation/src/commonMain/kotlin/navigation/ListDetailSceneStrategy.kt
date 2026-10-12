package navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

/**
 * A [Scene] that renders a list pane and a detail pane side-by-side in an adaptive two-pane layout.
 */
data class TwoPaneScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val listEntry: NavEntry<T>,
    val detailEntry: NavEntry<T>?,
    val detailPlaceholder: (@Composable () -> Unit)? = null,
    val splitWeights: Pair<Float, Float> = 0.42f to 0.58f,
) : Scene<T> {

    override val entries: List<NavEntry<T>> = listOfNotNull(listEntry, detailEntry)

    override val content: @Composable (() -> Unit) = {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(splitWeights.first)
                    .fillMaxHeight(),
            ) {
                listEntry.Content()
            }

            VerticalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            )

            Box(
                modifier = Modifier
                    .weight(splitWeights.second)
                    .fillMaxHeight(),
            ) {
                AnimatedContent(
                    targetState = detailEntry,
                    contentKey = { it?.contentKey },
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                ) { entry ->
                    if (entry != null) {
                        entry.Content()
                    } else if (detailPlaceholder != null) {
                        detailPlaceholder.invoke()
                    }
                }
            }
        }
    }
}

/**
 * A Navigation 3 [SceneStrategy] that displays entries marked with [listPane] and [detailPane]
 * side-by-side when [isDualPane] is true, or sequentially (via fallback single pane) when false.
 */
class ListDetailSceneStrategy<T : Any>(
    private val isDualPane: () -> Boolean,
    private val splitWeights: () -> Pair<Float, Float> = { 0.42f to 0.58f },
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        if (!isDualPane()) return null

        val lastEntry = entries.lastOrNull() ?: return null

        // Case 1: Last entry is detailPane and predecessor is listPane
        val isLastDetail = lastEntry.metadata[DetailPaneKey] == true
        if (isLastDetail && entries.size >= 2) {
            val listEntry = entries.dropLast(1).findLast { it.metadata[ListPaneKey] == true }
            if (listEntry != null) {
                return TwoPaneScene(
                    key = "TwoPane_${listEntry.contentKey}",
                    previousEntries = entries.dropLast(1),
                    listEntry = listEntry,
                    detailEntry = lastEntry,
                    detailPlaceholder = listEntry.metadata[DetailPlaceholderKey],
                    splitWeights = splitWeights(),
                )
            }
        }

        // Case 2: Last entry is listPane (with placeholder)
        if (lastEntry.metadata[ListPaneKey] == true) {
            val placeholder = lastEntry.metadata[DetailPlaceholderKey]
            if (placeholder != null) {
                return TwoPaneScene(
                    key = "TwoPane_${lastEntry.contentKey}",
                    previousEntries = entries.dropLast(1),
                    listEntry = lastEntry,
                    detailEntry = null,
                    detailPlaceholder = placeholder,
                    splitWeights = splitWeights(),
                )
            }
        }

        return null
    }

    companion object {
        fun listPane(detailPlaceholder: (@Composable () -> Unit)? = null): Map<String, Any> = metadata {
            put(ListPaneKey, true)
            if (detailPlaceholder != null) {
                put(DetailPlaceholderKey, detailPlaceholder)
            }
        }

        fun detailPane(): Map<String, Any> = metadata {
            put(DetailPaneKey, true)
        }

        private object ListPaneKey : NavMetadataKey<Boolean>
        private object DetailPaneKey : NavMetadataKey<Boolean>
        private object DetailPlaceholderKey : NavMetadataKey<@Composable () -> Unit>
    }
}
