package ly.david.musicsearch.data.database.dao

import androidx.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToPlaceListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel
import ly.david.musicsearch.shared.domain.sort.PlaceSortOption
import lydavidmusicsearchdatadatabase.Area_place
import kotlin.time.Clock
import kotlin.time.Instant

class PlaceDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.placeQueries

    fun upsert(
        oldId: String,
        place: PlaceMusicBrainzNetworkModel,
    ) {
        place.run {
            if (oldId != id) {
                delete(oldId)
            }
            transacter.upsert(
                id = id,
                name = name,
                disambiguation = disambiguation.orEmpty(),
                address = address,
                type = type.orEmpty(),
                type_id = typeId.orEmpty(),
                longitude = coordinates?.longitude,
                latitude = coordinates?.latitude,
                begin = lifeSpan?.begin.orEmpty(),
                end = lifeSpan?.end.orEmpty(),
                ended = lifeSpan?.ended == true,
            )
        }
    }

    fun upsertAll(places: List<PlaceMusicBrainzNetworkModel>) {
        transacter.transaction {
            places.forEach { place ->
                upsert(
                    oldId = place.id,
                    place = place,
                )
            }
        }
    }

    fun getPlaceForDetails(placeId: String): PlaceDetailsModel? {
        return transacter.getPlaceForDetails(
            placeId = placeId,
            mapper = ::mapToDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun mapToDetailsModel(
        id: String,
        name: String,
        disambiguation: String,
        address: String,
        type: String,
        longitude: Double?,
        latitude: Double?,
        begin: String,
        end: String,
        ended: Boolean,
        lastUpdated: Instant?,
    ) = PlaceDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        address = address,
        type = type,
        coordinates = CoordinatesUiModel(
            longitude = longitude,
            latitude = latitude,
        ),
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
        lastUpdated = lastUpdated ?: Clock.System.now(),
    )

    fun delete(id: String) {
        transacter.deletePlace(id)
    }

    // region places
    fun insertPlaceByArea(
        entityId: String,
        placeId: String,
    ) {
        transacter.insertOrIgnoreAreaPlace(
            Area_place(
                area_id = entityId,
                place_id = placeId,
            ),
        )
    }

    fun insertPlacesByArea(
        entityId: String,
        placeIds: List<String>,
    ) {
        transacter.transaction {
            placeIds.forEach { placeId ->
                insertPlaceByArea(
                    entityId = entityId,
                    placeId = placeId,
                )
            }
        }
    }

    fun deletePlacesByArea(areaId: String) {
        transacter.deletePlacesByArea(areaId)
    }

    fun getCountOfPlacesByArea(areaId: String): Int =
        getPlacesByAreaCountQuery(
            areaId = areaId,
            query = "",
        )
            .executeAsOne()
            .toInt()

    fun getPlaces(
        browseMethod: BrowseMethod,
        query: String,
        sortOption: PlaceSortOption,
    ): PagingSource<Int, PlaceListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllPlaces(
                query = query,
                sortOption = sortOption,
            )
        }

        is BrowseMethod.ByEntity -> {
            if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                getPlacesByCollection(
                    collectionId = browseMethod.entityId,
                    query = query,
                    sortOption = sortOption,
                )
            } else {
                getPlacesByArea(
                    areaId = browseMethod.entityId,
                    query = query,
                    sortOption = sortOption,
                )
            }
        }
    }

    fun observeCountOfPlaces(
        browseMethod: BrowseMethod,
        query: String,
    ): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                    getCountOfPlacesByCollectionQuery(
                        collectionId = browseMethod.entityId,
                        query = query,
                    )
                } else {
                    getPlacesByAreaCountQuery(
                        areaId = browseMethod.entityId,
                        query = query,
                    )
                }
            }

            BrowseMethod.All -> {
                getCountOfAllPlaces(query = query)
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllPlaces(
        query: String,
    ): Query<Long> = transacter.getCountOfAllPlaces(
        query = "%$query%",
    )

    private fun getAllPlaces(
        query: String,
        sortOption: PlaceSortOption,
    ): PagingSource<Int, PlaceListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllPlaces(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllPlaces(
                query = "%$query%",
                sortBy = sortOption.order.toLong(),
                limit = limit,
                offset = offset,
                mapper = ::mapToPlaceListItemModel,
            )
        },
    )

    private fun getPlacesByArea(
        areaId: String,
        query: String,
        sortOption: PlaceSortOption,
    ): PagingSource<Int, PlaceListItemModel> = QueryPagingSource(
        countQuery = getPlacesByAreaCountQuery(areaId, query),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getPlacesByArea(
                areaId = areaId,
                query = "%$query%",
                sortBy = sortOption.order.toLong(),
                limit = limit,
                offset = offset,
                mapper = ::mapToPlaceListItemModel,
            )
        },
    )

    private fun getPlacesByAreaCountQuery(
        areaId: String,
        query: String,
    ) = transacter.getNumberOfPlacesByArea(
        areaId = areaId,
        query = "%$query%",
    )

    private fun getPlacesByCollection(
        collectionId: String,
        query: String,
        sortOption: PlaceSortOption,
    ): PagingSource<Int, PlaceListItemModel> = QueryPagingSource(
        countQuery = getCountOfPlacesByCollectionQuery(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getPlacesByCollection(
                collectionId = collectionId,
                query = "%$query%",
                sortBy = sortOption.order.toLong(),
                limit = limit,
                offset = offset,
                mapper = ::mapToPlaceListItemModel,
            )
        },
    )

    private fun getCountOfPlacesByCollectionQuery(
        collectionId: String,
        query: String,
    ): Query<Long> = transacter.getNumberOfPlacesByCollection(
        collectionId = collectionId,
        query = "%$query%",
    )
    // endregion
}
