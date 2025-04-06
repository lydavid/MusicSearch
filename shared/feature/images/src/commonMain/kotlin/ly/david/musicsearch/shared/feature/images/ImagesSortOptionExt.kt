package ly.david.musicsearch.shared.feature.images

import ly.david.musicsearch.shared.domain.coverarts.ImagesSortOption
import ly.david.musicsearch.shared.strings.AppStrings

internal fun ImagesSortOption.getLabel(strings: AppStrings): String {
    return when (this) {
        ImagesSortOption.ALPHABETICALLY -> strings.alphabetically
        ImagesSortOption.ALPHABETICALLY_REVERSE -> strings.alphabeticallyReverse
        ImagesSortOption.RECENTLY_ADDED -> strings.recentlyAdded
        ImagesSortOption.LEAST_RECENTLY_ADDED -> strings.leastRecentlyAdded
    }
}
