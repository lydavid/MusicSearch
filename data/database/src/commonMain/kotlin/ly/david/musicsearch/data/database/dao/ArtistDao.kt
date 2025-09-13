package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToArtistListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import lydavidmusicsearchdatadatabase.Artists_by_entity
import kotlin.time.Clock
import kotlin.time.Instant

class ArtistDao(
    database: Database,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.artistQueries

    fun upsert(artist: ArtistMusicBrainzNetworkModel) {
        artist.run {
            transacter.upsert(
                id = id,
                name = name,
                sort_name = sortName,
                disambiguation = disambiguation.orEmpty(),
                type = type.orEmpty(),
                type_id = typeId.orEmpty(),
                gender = gender.orEmpty(),
                ipis = ipis.orEmpty(),
                isnis = isnis.orEmpty(),
                country_code = countryCode.orEmpty(),
                begin = lifeSpan?.begin.orEmpty(),
                end = lifeSpan?.end.orEmpty(),
                ended = lifeSpan?.ended == true,
                area_id = area?.id.orEmpty(),
            )
        }
    }

    fun upsertAll(artists: List<ArtistMusicBrainzNetworkModel>) {
        transacter.transaction {
            artists.forEach { artist ->
                upsert(artist)
            }
        }
    }

    fun getArtistForDetails(
        artistId: String,
        listenBrainzUsername: String,
    ): ArtistDetailsModel? {
        val artist = transacter.getArtistForDetails(
            artistId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull() ?: return null

        val listenCount = if (listenBrainzUsername.isNotEmpty()) {
            transacter.getListenCountByArtist(
                artistId = artistId,
                username = listenBrainzUsername,
            ).executeAsOne()
        } else {
            null
        }

        return artist.copy(
            listenCount = listenCount,
        )
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        sortName: String,
        disambiguation: String,
        type: String,
        gender: String,
        ipis: List<String>,
        isnis: List<String>,
        begin: String,
        end: String,
        ended: Boolean,
        areaId: String,
        areaName: String?,
        countryCode: String?,
        visitedArea: Boolean?,
        lastUpdated: Instant?,
    ): ArtistDetailsModel {
        val area = if (areaName != null) {
            AreaListItemModel(
                id = areaId,
                name = areaName,
                countryCodes = listOfNotNull(countryCode).toPersistentList(),
                visited = visitedArea == true,
            )
        } else {
            null
        }
        return ArtistDetailsModel(
            id = id,
            name = name,
            sortName = sortName,
            disambiguation = disambiguation,
            type = type,
            gender = gender,
            ipis = ipis.toPersistentList(),
            isnis = isnis.toPersistentList(),
            lifeSpan = LifeSpanUiModel(
                begin = begin,
                end = end,
                ended = ended,
            ),
            areaListItemModel = area,
            lastUpdated = lastUpdated ?: Clock.System.now(),
        )
    }

    fun delete(artistId: String) {
        transacter.deleteArtist(artistId)
    }

    fun insertArtistsByEntity(
        entityId: String,
        artistIds: List<String>,
    ) {
        transacter.transaction {
            artistIds.forEach { artistId ->
                transacter.insertOrIgnoreArtistByEntity(
                    Artists_by_entity(
                        entity_id = entityId,
                        artist_id = artistId,
                    ),
                )
            }
        }
    }

    fun deleteArtistLinksByEntity(entityId: String) {
        transacter.deleteArtistLinksByEntity(entityId)
    }

    fun getCountOfArtistsByEntity(entityId: String): Int =
        getCountOfArtistsByEntityQuery(
            entityId = entityId,
            query = "",
        )
            .executeAsOne()
            .toInt()

    private fun getCountOfArtistsByEntityQuery(
        entityId: String,
        query: String,
    ) = transacter.getNumberOfArtistsByEntity(
        entityId = entityId,
        query = "%$query%",
    )

    fun getArtists(
        browseMethod: BrowseMethod,
        query: String,
    ): PagingSource<Int, ArtistListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllArtists(
                query = query,
            )
        }

        is BrowseMethod.ByEntity -> {
            if (browseMethod.entity == MusicBrainzEntityType.COLLECTION) {
                getArtistsByCollection(
                    collectionId = browseMethod.entityId,
                    query = query,
                )
            } else {
                getArtistsByEntity(
                    entityId = browseMethod.entityId,
                    query = query,
                )
            }
        }
    }

    fun observeCountOfArtists(browseMethod: BrowseMethod): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entity == MusicBrainzEntityType.COLLECTION) {
                    collectionEntityDao.getCountOfEntitiesByCollectionQuery(
                        collectionId = browseMethod.entityId,
                    )
                } else {
                    getCountOfArtistsByEntityQuery(
                        entityId = browseMethod.entityId,
                        query = "",
                    )
                }
            }

            else -> {
                getCountOfAllArtists(query = "")
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllArtists(
        query: String,
    ): Query<Long> = transacter.getCountOfAllArtists(
        query = "%$query%",
    )

    private fun getAllArtists(
        query: String,
    ) = QueryPagingSource(
        countQuery = getCountOfAllArtists(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllArtists(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToArtistListItemModel,
            )
        },
    )

    private fun getArtistsByEntity(
        entityId: String,
        query: String,
    ) = QueryPagingSource(
        countQuery = getCountOfArtistsByEntityQuery(
            entityId = entityId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getArtistsByEntity(
                entityId = entityId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToArtistListItemModel,
            )
        },
    )

    private fun getArtistsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, ArtistListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfArtistsByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getArtistsByCollection(
                collectionId = collectionId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToArtistListItemModel,
            )
        },
    )
}
