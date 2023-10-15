package ly.david.musicsearch.domain.area

import ly.david.musicsearch.core.models.area.AreaScaffoldModel

interface AreaRepository {
    suspend fun lookupArea(areaId: String): AreaScaffoldModel
}
