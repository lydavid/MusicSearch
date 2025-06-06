package ly.david.musicsearch.shared.domain.area

import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel

interface AreaRepository {
    suspend fun lookupArea(
        areaId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): AreaDetailsModel
}
