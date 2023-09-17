package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Area
import lydavidmusicsearchdatadatabase.Area_place
import lydavidmusicsearchdatadatabase.Place

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

    // region places by area
    fun linkAreaWithPlaces(
        areaId: String,
        placeIds: List<String>,
    ) {
        transacter.transaction {
            placeIds.forEach { placeId ->
                insert(
                    areaId = areaId,
                    placeId = placeId,
                )
            }
        }
    }

    fun deletePlacesByArea(areaId: String) {
        transacter.deletePlacesByArea(areaId)
    }

    fun getNumberOfPlacesByArea(areaId: String): Int =
        transacter.getNumberOfPlacesByArea(
            areaId = areaId,
            query = "%%",
        ).executeAsOne().toInt()

    fun getPlacesByArea(
        areaId: String,
        query: String,
    ): PagingSource<Int, Place> = QueryPagingSource(
        countQuery = transacter.getNumberOfPlacesByArea(
            areaId = areaId,
            query = query,
        ),
        transacter = transacter,
        context = Dispatchers.IO,
    ) { limit, offset ->
        transacter.getPlacesByArea(
            areaId = areaId,
            query = query,
            limit = limit,
            offset = offset,
        )
    }
    // endregion
}
