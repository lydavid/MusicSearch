package ly.david.musicsearch.shared.domain.area

import ly.david.musicsearch.shared.domain.area.AreaDetailsModel

interface AreaRepository {
    suspend fun lookupArea(areaId: String): AreaDetailsModel
}
