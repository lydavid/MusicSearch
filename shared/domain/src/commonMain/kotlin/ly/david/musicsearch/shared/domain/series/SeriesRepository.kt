package ly.david.musicsearch.shared.domain.series

import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel

interface SeriesRepository {
    suspend fun lookupSeries(
        seriesId: String,
        forceRefresh: Boolean,
    ): SeriesDetailsModel
}
