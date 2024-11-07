package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToAreaListItemModel
import ly.david.musicsearch.data.database.mapper.mapToPlaceListItemModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import lydavidmusicsearchdatadatabase.Area_place

class AreaPlaceDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.area_placeQueries

    fun insert(
        areaId: String,
        placeId: String,
    ) {
        transacter.insert(
            Area_place(
                area_id = areaId,
                place_id = placeId,
            ),
        )
    }

    fun getAreaByPlace(placeId: String): AreaListItemModel? =
        transacter.getAreasByPlace(
            placeId = placeId,
            mapper = ::mapToAreaListItemModel,
        ).executeAsList().findByTypePriority()

    private fun List<AreaListItemModel>.findByTypePriority(
        typePriorities: List<String?> = listOf(
            null, // The area part of a place lookup has a null type
            "District",
            "City",
            "Municipality",
            "County",
            "Subdivision",
            "Country",
        ),
    ): AreaListItemModel? {
        for (type in typePriorities) {
            firstOrNull { it.type == type }?.let { return it }
        }
        return null
    }

    fun deleteAreaPlaceLink(placeId: String) {
        transacter.deleteAreaPlaceLink(placeId = placeId)
    }

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

    fun getNumberOfPlacesByArea(areaId: String): Flow<Int> =
        transacter.getNumberOfPlacesByArea(
            areaId = areaId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getPlacesByArea(
        areaId: String,
        query: String,
    ): PagingSource<Int, PlaceListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfPlacesByArea(
            areaId = areaId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getPlacesByArea(
                areaId = areaId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToPlaceListItemModel,
            )
        },
    )
    // endregion
}
