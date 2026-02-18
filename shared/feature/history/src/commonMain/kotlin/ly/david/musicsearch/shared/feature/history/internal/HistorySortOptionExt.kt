package ly.david.musicsearch.shared.feature.history.internal

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.alphabetically
import musicsearch.ui.common.generated.resources.alphabeticallyReverse
import musicsearch.ui.common.generated.resources.leastRecentlyVisited
import musicsearch.ui.common.generated.resources.leastVisited
import musicsearch.ui.common.generated.resources.mostVisited
import musicsearch.ui.common.generated.resources.recentlyVisited
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HistorySortOption.getLabel(): String {
    return stringResource(
        when (this) {
            HistorySortOption.ALPHABETICALLY -> Res.string.alphabetically
            HistorySortOption.ALPHABETICALLY_REVERSE -> Res.string.alphabeticallyReverse
            HistorySortOption.LEAST_RECENTLY_VISITED -> Res.string.leastRecentlyVisited
            HistorySortOption.RECENTLY_VISITED -> Res.string.recentlyVisited
            HistorySortOption.MOST_VISITED -> Res.string.mostVisited
            HistorySortOption.LEAST_VISITED -> Res.string.leastVisited
        },
    )
}
