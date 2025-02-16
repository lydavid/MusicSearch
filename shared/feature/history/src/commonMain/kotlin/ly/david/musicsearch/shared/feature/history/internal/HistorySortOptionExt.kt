package ly.david.musicsearch.shared.feature.history.internal

import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.strings.AppStrings

internal fun HistorySortOption.getLabel(strings: AppStrings): String {
    return when (this) {
        HistorySortOption.ALPHABETICALLY -> strings.alphabetically
        HistorySortOption.ALPHABETICALLY_REVERSE -> strings.alphabeticallyReverse
        HistorySortOption.LEAST_RECENTLY_VISITED -> strings.leastRecentlyVisited
        HistorySortOption.RECENTLY_VISITED -> strings.recentlyVisited
        HistorySortOption.MOST_VISITED -> strings.mostVisited
        HistorySortOption.LEAST_VISITED -> strings.leastVisited
    }
}
