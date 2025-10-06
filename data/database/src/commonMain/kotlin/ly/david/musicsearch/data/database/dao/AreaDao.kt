package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToAreaListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseEventMusicBrainzModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.area.ReleaseEvent
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import lydavidmusicsearchdatadatabase.Area
import lydavidmusicsearchdatadatabase.AreaQueries
import lydavidmusicsearchdatadatabase.Areas_by_entity
import kotlin.time.Clock
import kotlin.time.Instant

interface AreaDao : EntityDao {
    fun insert(area: AreaMusicBrainzNetworkModel?)
    fun upsert(
        oldAreaId: String,
        area: AreaMusicBrainzNetworkModel?,
    )

    fun getAreaForDetails(areaId: String): AreaDetailsModel?
    fun delete(areaId: String)

    fun insertAll(areas: List<AreaMusicBrainzNetworkModel>)
    fun upsertAll(areas: List<AreaMusicBrainzNetworkModel>)
    fun getAreas(
        browseMethod: BrowseMethod,
        query: String,
    ): PagingSource<Int, AreaListItemModel>

    fun observeCountOfAreas(browseMethod: BrowseMethod): Flow<Int>

    fun getAreaByPlace(placeId: String): AreaListItemModel?
    fun deleteAreaPlaceLink(placeId: String)

    fun getCountriesByRelease(releaseId: String): List<ReleaseEvent>
    fun linkCountriesByRelease(
        releaseId: String,
        releaseEvents: List<ReleaseEventMusicBrainzModel>?,
    )

    fun deleteCountriesByReleaseLinks(releaseId: String)
}

class AreaDaoImpl(
    database: Database,
    private val countryCodeDao: CountryCodeDao,
    private val releaseCountryDao: ReleaseCountryDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : AreaDao {
    override val transacter: AreaQueries = database.areaQueries

    override fun insert(area: AreaMusicBrainzNetworkModel?) {
        area?.run {
            transacter.insertArea(
                Area(
                    id = id,
                    name = name,
                    sort_name = sortName,
                    disambiguation = disambiguation.orEmpty(),
                    type = type.orEmpty(),
                    type_id = typeId.orEmpty(),
                    begin = lifeSpan?.begin.orEmpty(),
                    end = lifeSpan?.end.orEmpty(),
                    ended = lifeSpan?.ended == true,
                ),
            )
            countryCodeDao.insertCountryCodesForArea(
                areaId = area.id,
                countryCodes = area.countryCodes.orEmpty(),
            )
        }
    }

    override fun upsert(
        oldAreaId: String,
        area: AreaMusicBrainzNetworkModel?,
    ) {
        area?.run {
            if (oldAreaId != id) {
                delete(oldAreaId)
            }
            transacter.upsert(
                id = id,
                name = name,
                sort_name = sortName,
                disambiguation = disambiguation.orEmpty(),
                type = type.orEmpty(),
                type_id = typeId.orEmpty(),
                begin = lifeSpan?.begin.orEmpty(),
                end = lifeSpan?.end.orEmpty(),
                ended = lifeSpan?.ended == true,
            )
            countryCodeDao.insertCountryCodesForArea(
                areaId = area.id,
                countryCodes = area.countryCodes.orEmpty(),
            )
        }
    }

    override fun getAreaForDetails(areaId: String): AreaDetailsModel? {
        return transacter.getAreaForDetails(
            areaId = areaId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String,
        type: String,
        begin: String,
        end: String,
        ended: Boolean,
        countryCode: String?,
        lastUpdated: Instant?,
    ) = AreaDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
        countryCode = countryCode.orEmpty(),
        lastUpdated = lastUpdated ?: Clock.System.now(),
    )

    override fun delete(areaId: String) {
        withTransaction {
            countryCodeDao.delete(areaId)
            transacter.deleteArea(areaId)
        }
    }

    // region areas
    override fun insertAll(areas: List<AreaMusicBrainzNetworkModel>) {
        return transacter.transaction {
            areas.forEach { area ->
                insert(area)
            }
        }
    }

    override fun upsertAll(areas: List<AreaMusicBrainzNetworkModel>) {
        return transacter.transaction {
            areas.forEach { area ->
                upsert(
                    oldAreaId = area.id,
                    area = area,
                )
            }
        }
    }

    override fun getAreas(
        browseMethod: BrowseMethod,
        query: String,
    ): PagingSource<Int, AreaListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllAreas(
                query = query,
            )
        }

        is BrowseMethod.ByEntity -> {
            getAreasByCollection(
                mbid = browseMethod.entityId,
                query = query,
            )
        }
    }

    override fun observeCountOfAreas(browseMethod: BrowseMethod): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                collectionEntityDao.getCountOfEntitiesByCollectionQuery(
                    collectionId = browseMethod.entityId,
                )
            }

            else -> {
                getCountOfAllAreas(query = "")
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllAreas(
        query: String,
    ): Query<Long> = transacter.getCountOfAllAreas(
        query = "%$query%",
    )

    private fun getAllAreas(
        query: String,
    ) = QueryPagingSource(
        countQuery = getCountOfAllAreas(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllAreas(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToAreaListItemModel,
            )
        },
    )

    private fun getAreasByCollection(
        mbid: String,
        query: String,
    ) = QueryPagingSource(
        countQuery = transacter.getNumberOfAreasByCollection(
            collectionId = mbid,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAreasByCollection(
                collectionId = mbid,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToAreaListItemModel,
            )
        },
    )
    // endregion

    override fun getAreaByPlace(placeId: String): AreaListItemModel? =
        transacter.getAreasByPlace(
            placeId = placeId,
            mapper = ::mapToAreaListItemModel,
        ).executeAsList().findByTypePriority()

    // TODO: may be inaccurate if an area is contained within another area but has the same type
    private fun List<AreaListItemModel>.findByTypePriority(
        typePriorities: List<String> = listOf(
            "", // The area part of a place lookup has a null type
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

    override fun deleteAreaPlaceLink(placeId: String) {
        transacter.deleteAreaPlaceLink(placeId = placeId)
    }

    // region countries by release
    override fun linkCountriesByRelease(
        releaseId: String,
        releaseEvents: List<ReleaseEventMusicBrainzModel>?,
    ) {
        transacter.transaction {
            releaseEvents?.forEach { releaseEvent ->
                val areaId = releaseEvent.area?.id ?: return@forEach
                transacter.insertOrIgnoreAreasByEntity(
                    areas_by_entity = Areas_by_entity(
                        entity_id = releaseId,
                        area_id = areaId,
                    ),
                )
                releaseCountryDao.insertOrIgnore(
                    areaId = areaId,
                    releaseId = releaseId,
                    date = releaseEvent.date,
                )
            }
        }
    }

    override fun getCountriesByRelease(
        releaseId: String,
    ): List<ReleaseEvent> = transacter.getCountriesByRelease(
        releaseId = releaseId,
        mapper = { id, name, date, countryCode, visited ->
            ReleaseEvent(
                id = id,
                name = name,
                date = date,
                countryCode = countryCode,
                visited = visited,
            )
        },
    ).executeAsList()

    override fun deleteCountriesByReleaseLinks(releaseId: String) {
        releaseCountryDao.deleteCountriesByReleaseLinks(releaseId = releaseId)
    }
    // endregion
}
