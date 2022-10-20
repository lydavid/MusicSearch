package ly.david.mbjc.ui.common.paging

import androidx.paging.PagingConfig
import ly.david.data.network.api.SEARCH_BROWSE_LIMIT

internal object MusicBrainzPagingConfig {

    /**
     * Specifically chosen so that we don't fetch multiple pages before loading footer disappears.
     */
    private const val PREFETCH_DISTANCE = 1

    /**
     * Common [PagingConfig] for consistency.
     */
    val pagingConfig = PagingConfig(
        pageSize = SEARCH_BROWSE_LIMIT,
        initialLoadSize = SEARCH_BROWSE_LIMIT,
        prefetchDistance = PREFETCH_DISTANCE,
    )
}
