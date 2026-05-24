package ly.david.musicsearch.shared.domain.series

import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import kotlin.time.Instant

interface SeriesRepository {
    suspend fun lookupEntity(
        entityId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): SeriesDetailsModel
}
