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
import ly.david.musicsearch.data.database.mapper.mapToAreaListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseEventMusicBrainzModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.area.ReleaseEvent
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import lydavidmusicsearchdatadatabase.Area
import lydavidmusicsearchdatadatabase.AreaQueries
import lydavidmusicsearchdatadatabase.Areas_by_entity

class AreaDao(
    database: Database,
    private val countryCodeDao: CountryCodeDao,
    private val releaseCountryDao: ReleaseCountryDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter: AreaQueries = database.areaQueries

    fun insert(area: AreaMusicBrainzNetworkModel?) {
        area?.run {
            transacter.insertArea(
                Area(
                    id = id,
                    name = name,
                    sort_name = sortName,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    begin = lifeSpan?.begin,
                    end = lifeSpan?.end,
                    ended = lifeSpan?.ended,
                ),
            )
            countryCodeDao.insertCountryCodesForArea(
                areaId = area.id,
                countryCodes = area.countryCodes.orEmpty(),
            )
        }
    }

    fun insertReplace(area: AreaMusicBrainzNetworkModel?) {
        area?.run {
            transacter.insertOrReplaceArea(
                Area(
                    id = id,
                    name = name,
                    sort_name = sortName,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    begin = lifeSpan?.begin,
                    end = lifeSpan?.end,
                    ended = lifeSpan?.ended,
                ),
            )
            countryCodeDao.insertCountryCodesForArea(
                areaId = area.id,
                countryCodes = area.countryCodes.orEmpty(),
            )
        }
    }

    fun insertAll(areas: List<AreaMusicBrainzNetworkModel>) {
        return transacter.transaction {
            areas.forEach { area ->
                insert(area)
            }
        }
    }

    fun insertReplaceAll(areas: List<AreaMusicBrainzNetworkModel>) {
        return transacter.transaction {
            areas.forEach { area ->
                insertReplace(area)
            }
        }
    }

    fun getAreaForDetails(areaId: String): AreaDetailsModel? {
        return transacter.getAreaForDetails(
            areaId = areaId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    fun getAreas(
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

    fun observeCountOfAreas(browseMethod: BrowseMethod): Flow<Int> =
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

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
        begin: String?,
        end: String?,
        ended: Boolean?,
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

    fun delete(areaId: String) {
        withTransaction {
            countryCodeDao.delete(areaId)
            transacter.deleteArea(areaId)
        }
    }

    // TODO: may be inaccurate if an area is contained within another area but has the same type
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

    fun getAreaByPlace(placeId: String): AreaListItemModel? =
        transacter.getAreasByPlace(
            placeId = placeId,
            mapper = ::mapToAreaListItemModel,
        ).executeAsList().findByTypePriority()

    fun deleteAreaPlaceLink(placeId: String) {
        transacter.deleteAreaPlaceLink(placeId = placeId)
    }

    // region countries by release
    fun linkCountriesByRelease(
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

    fun getCountriesByRelease(
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

    fun deleteCountriesByReleaseLinks(releaseId: String) {
        releaseCountryDao.deleteCountriesByReleaseLinks(releaseId = releaseId)
    }
    // endregion
}
