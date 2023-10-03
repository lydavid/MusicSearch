package ly.david.musicsearch.domain.paging

import app.cash.paging.PagingConfig
import ly.david.data.musicbrainz.api.SEARCH_BROWSE_LIMIT

object MusicBrainzPagingConfig {

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
