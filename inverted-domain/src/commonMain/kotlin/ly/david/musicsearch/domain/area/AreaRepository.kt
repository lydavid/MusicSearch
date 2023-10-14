package ly.david.musicsearch.domain.area

import ly.david.musicsearch.data.core.area.AreaScaffoldModel

interface AreaRepository {
    suspend fun lookupArea(areaId: String): AreaScaffoldModel
}
