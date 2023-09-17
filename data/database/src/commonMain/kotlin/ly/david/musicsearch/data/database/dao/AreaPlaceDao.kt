package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Area
import lydavidmusicsearchdatadatabase.Area_place

class AreaPlaceDao(
    database: Database,
) {
    private val transacter = database.area_placeQueries

    fun insert(
        areaId: String,
        placeId: String,
    ) {
        transacter.insert(
            Area_place(
                area_id = areaId,
                place_id = placeId,
            )
        )
    }

    fun getAreaByPlace(placeId: String): Area? =
        transacter.getAreaByPlace(placeId).executeAsOneOrNull()
}
