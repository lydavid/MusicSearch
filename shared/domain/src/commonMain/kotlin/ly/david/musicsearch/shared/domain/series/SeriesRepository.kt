package ly.david.musicsearch.shared.domain.series

import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel

interface SeriesRepository {
    suspend fun lookupSeries(
        seriesId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): SeriesDetailsModel
}
