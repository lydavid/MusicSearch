package ly.david.musicsearch.shared.domain.series

import ly.david.musicsearch.core.models.series.SeriesDetailsModel

interface SeriesRepository {
    suspend fun lookupSeries(seriesId: String): SeriesDetailsModel
}
