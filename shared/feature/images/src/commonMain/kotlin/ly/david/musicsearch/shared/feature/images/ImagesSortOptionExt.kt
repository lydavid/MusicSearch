package ly.david.musicsearch.shared.feature.images

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.alphabetically
import musicsearch.ui.common.generated.resources.alphabeticallyReverse
import musicsearch.ui.common.generated.resources.leastRecentlyAdded
import musicsearch.ui.common.generated.resources.recentlyAdded
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ImagesSortOption.getLabel(): String {
    return stringResource(
        when (this) {
            ImagesSortOption.ALPHABETICALLY -> Res.string.alphabetically
            ImagesSortOption.ALPHABETICALLY_REVERSE -> Res.string.alphabeticallyReverse
            ImagesSortOption.RECENTLY_ADDED -> Res.string.recentlyAdded
            ImagesSortOption.LEAST_RECENTLY_ADDED -> Res.string.leastRecentlyAdded
        },
    )
}
