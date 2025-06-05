package ly.david.musicsearch.shared.domain.area

import ly.david.musicsearch.shared.domain.details.AreaDetailsModel

interface AreaRepository {
    suspend fun lookupArea(
        areaId: String,
        forceRefresh: Boolean,
    ): AreaDetailsModel
}
