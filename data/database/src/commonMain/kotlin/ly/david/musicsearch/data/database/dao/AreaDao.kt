package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToAreaListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.area.AreaDetailsModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import lydavidmusicsearchdatadatabase.Area
import lydavidmusicsearchdatadatabase.AreaQueries

class AreaDao(
    database: Database,
    private val countryCodeDao: CountryCodeDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter: AreaQueries = database.areaQueries

    fun insert(area: AreaMusicBrainzModel?) {
        area?.run {
            transacter.insert(
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

    fun insertReplace(area: AreaMusicBrainzModel?) {
        area?.run {
            transacter.insertReplace(
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

    fun insertAll(areas: List<AreaMusicBrainzModel>): Int {
        return transacter.transactionWithResult {
            areas.forEach { area ->
                insert(area)
            }
            areas.size
        }
    }

    fun getAreaForDetails(areaId: String): AreaDetailsModel? {
        return transacter.getArea(
            id = areaId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    fun getAreas(
        mbid: String?,
        query: String,
    ): PagingSource<Int, AreaListItemModel> = when {
        mbid == null -> {
            getAllAreas(
                query = query,
            )
        }

        else -> {
            getAreasByCollection(
                mbid = mbid,
                query = query,
            )
        }
    }

    private fun getAllAreas(
        query: String,
    ) = QueryPagingSource(
        countQuery = transacter.getNumberOfAreas(
            query = "%$query%",
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
        countryCodes = listOfNotNull(countryCode),
    )

    fun delete(areaId: String) {
        withTransaction {
            countryCodeDao.delete(areaId)
            transacter.delete(areaId)
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
}
