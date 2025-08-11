package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToPlaceListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel
import lydavidmusicsearchdatadatabase.Area_place
import lydavidmusicsearchdatadatabase.Place

class PlaceDao(
    database: Database,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.placeQueries

    fun insert(place: PlaceMusicBrainzNetworkModel) {
        place.run {
            transacter.insertPlace(
                Place(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    address = address,
                    type = type,
                    type_id = typeId,
                    longitude = coordinates?.longitude,
                    latitude = coordinates?.latitude,
                    begin = lifeSpan?.begin,
                    end = lifeSpan?.end,
                    ended = lifeSpan?.ended,
                ),
            )
        }
    }

    fun insertAll(places: List<PlaceMusicBrainzNetworkModel>) {
        transacter.transaction {
            places.forEach { place ->
                insert(place)
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
        disambiguation: String?,
        address: String,
        type: String?,
        longitude: Double?,
        latitude: Double?,
        begin: String?,
        end: String?,
        ended: Boolean?,
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
    ): PagingSource<Int, PlaceListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllPlaces(
                query = query,
            )
        }

        is BrowseMethod.ByEntity -> {
            if (browseMethod.entity == MusicBrainzEntity.COLLECTION) {
                getPlacesByCollection(
                    collectionId = browseMethod.entityId,
                    query = query,
                )
            } else {
                getPlacesByArea(
                    areaId = browseMethod.entityId,
                    query = query,
                )
            }
        }
    }

    fun observeCountOfPlaces(browseMethod: BrowseMethod): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entity == MusicBrainzEntity.COLLECTION) {
                    collectionEntityDao.getCountOfEntitiesByCollectionQuery(
                        collectionId = browseMethod.entityId,
                    )
                } else {
                    getPlacesByAreaCountQuery(
                        areaId = browseMethod.entityId,
                        query = "",
                    )
                }
            }

            else -> {
                getCountOfAllPlaces(query = "")
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
    ): PagingSource<Int, PlaceListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllPlaces(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllPlaces(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToPlaceListItemModel,
            )
        },
    )

    private fun getPlacesByArea(
        areaId: String,
        query: String,
    ): PagingSource<Int, PlaceListItemModel> = QueryPagingSource(
        countQuery = getPlacesByAreaCountQuery(areaId, query),
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
    ): PagingSource<Int, PlaceListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfPlacesByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getPlacesByCollection(
                collectionId = collectionId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToPlaceListItemModel,
            )
        },
    )
    // endregion
}
