package ly.david.musicsearch.shared.domain.paging

import androidx.paging.PagingConfig

object CommonPagingConfig {

    private const val PAGING_PAGE_SIZE = 100
    private const val PREFETCH_DISTANCE = 100

    /**
     * Common [app.cash.paging.PagingConfig] for consistency.
     */
    val pagingConfig = PagingConfig(
        pageSize = PAGING_PAGE_SIZE,
        initialLoadSize = PAGING_PAGE_SIZE,
        prefetchDistance = PREFETCH_DISTANCE,
    )
}
