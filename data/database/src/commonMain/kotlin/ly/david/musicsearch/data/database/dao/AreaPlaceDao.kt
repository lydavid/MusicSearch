package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Mb_area
import lydavidmusicsearchdatadatabase.Mb_area_place

class AreaPlaceDao(
    database: Database,
) {
    private val transacter = database.mb_area_placeQueries

    fun insert(
        areaId: String,
        placeId: String,
    ) {
        transacter.insert(
            Mb_area_place(
                area_id = areaId,
                place_id = placeId,
            )
        )
    }

    fun getAreaByPlace(placeId: String): Mb_area? =
        transacter.getAreaByPlace(placeId).executeAsOneOrNull()
}
