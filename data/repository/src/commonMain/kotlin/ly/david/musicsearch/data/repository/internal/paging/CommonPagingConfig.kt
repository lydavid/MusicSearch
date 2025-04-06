package ly.david.musicsearch.data.repository.internal.paging

import app.cash.paging.PagingConfig
import ly.david.musicsearch.data.musicbrainz.SEARCH_BROWSE_LIMIT

internal object CommonPagingConfig {

    private const val PREFETCH_DISTANCE = 100

    /**
     * Common [PagingConfig] for consistency.
     */
    val pagingConfig = PagingConfig(
        pageSize = SEARCH_BROWSE_LIMIT,
        initialLoadSize = SEARCH_BROWSE_LIMIT,
        prefetchDistance = PREFETCH_DISTANCE,
    )
}
