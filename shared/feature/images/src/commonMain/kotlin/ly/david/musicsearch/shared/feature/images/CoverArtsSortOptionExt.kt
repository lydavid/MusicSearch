package ly.david.musicsearch.shared.feature.images

import ly.david.musicsearch.shared.domain.coverarts.CoverArtsSortOption
import ly.david.musicsearch.shared.strings.AppStrings

internal fun CoverArtsSortOption.getLabel(strings: AppStrings): String {
    return when (this) {
        CoverArtsSortOption.ALPHABETICALLY -> strings.alphabetically
        CoverArtsSortOption.ALPHABETICALLY_REVERSE -> strings.alphabeticallyReverse
        CoverArtsSortOption.RECENTLY_ADDED -> strings.recentlyAdded
        CoverArtsSortOption.LEAST_RECENTLY_ADDED -> strings.leastRecentlyAdded
    }
}
