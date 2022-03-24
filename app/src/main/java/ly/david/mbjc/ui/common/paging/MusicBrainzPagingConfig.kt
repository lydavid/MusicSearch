package ly.david.mbjc.ui.common.paging

import androidx.paging.PagingConfig
import ly.david.mbjc.data.network.PREFETCH_DISTANCE
import ly.david.mbjc.data.network.SEARCH_BROWSE_LIMIT

object MusicBrainzPagingConfig {

    /**
     * Common [PagingConfig] for consistency.
     */
    val pagingConfig = PagingConfig(
        pageSize = SEARCH_BROWSE_LIMIT,
        initialLoadSize = SEARCH_BROWSE_LIMIT,
        prefetchDistance = PREFETCH_DISTANCE,
    )
}
