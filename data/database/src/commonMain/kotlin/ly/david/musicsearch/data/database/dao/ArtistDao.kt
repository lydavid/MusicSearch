package ly.david.musicsearch.data.database.dao

import androidx.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToArtistListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.NUMBER_OF_LATEST_LISTENS_TO_SHOW
import ly.david.musicsearch.shared.domain.artist.ArtistGender
import ly.david.musicsearch.shared.domain.artist.ArtistType
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.list.ArtistSortOption
import ly.david.musicsearch.shared.domain.listen.ListenWithRecording
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import lydavidmusicsearchdatadatabase.Artists_by_entity
import kotlin.time.Clock
import kotlin.time.Instant

class ArtistDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.artistQueries

    fun upsert(
        oldId: String,
        artist: ArtistMusicBrainzNetworkModel,
    ) {
        artist.run {
            if (oldId != id) {
                delete(oldId)
            }
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
                upsert(
                    oldId = artist.id,
                    artist = artist,
                )
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

        val latestListens = if (listenBrainzUsername.isNotEmpty()) {
            transacter.getLatestListensByArtist(
                artistId = artistId,
                username = listenBrainzUsername,
                limit = NUMBER_OF_LATEST_LISTENS_TO_SHOW,
                mapper = { id, name, disambiguation, listenedAtMs ->
                    ListenWithRecording(
                        id = id,
                        name = name,
                        disambiguation = disambiguation,
                        listenedMs = listenedAtMs,
                    )
                },
            ).executeAsList().toPersistentList()
        } else {
            persistentListOf()
        }

        return artist.copy(
            listenCount = listenCount,
            latestListens = latestListens,
        )
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        sortName: String,
        disambiguation: String,
        typeId: String,
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
            type = ArtistType.fromId(typeId),
            gender = ArtistGender.fromId(gender),
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
        sortOption: ArtistSortOption,
    ): PagingSource<Int, ArtistListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllArtists(
                query = query,
                sortOption = sortOption,
            )
        }

        is BrowseMethod.ByEntity -> {
            if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                getArtistsByCollection(
                    collectionId = browseMethod.entityId,
                    query = query,
                    sortOption = sortOption,
                )
            } else {
                getArtistsByEntity(
                    entityId = browseMethod.entityId,
                    query = query,
                    sortOption = sortOption,
                )
            }
        }
    }

    fun observeCountOfArtists(
        browseMethod: BrowseMethod,
        query: String,
    ): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                    getCountOfArtistsByCollectionQuery(
                        collectionId = browseMethod.entityId,
                        query = query,
                    )
                } else {
                    getCountOfArtistsByEntityQuery(
                        entityId = browseMethod.entityId,
                        query = query,
                    )
                }
            }

            BrowseMethod.All -> {
                getCountOfAllArtists(query = query)
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
        sortOption: ArtistSortOption,
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
                sortBy = sortOption.order.toLong(),
                mapper = ::mapToArtistListItemModel,
            )
        },
    )

    private fun getArtistsByEntity(
        entityId: String,
        query: String,
        sortOption: ArtistSortOption,
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
                sortBy = sortOption.order.toLong(),
                limit = limit,
                offset = offset,
                mapper = ::mapToArtistListItemModel,
            )
        },
    )

    private fun getArtistsByCollection(
        collectionId: String,
        query: String,
        sortOption: ArtistSortOption,
    ): PagingSource<Int, ArtistListItemModel> = QueryPagingSource(
        countQuery = getCountOfArtistsByCollectionQuery(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getArtistsByCollection(
                collectionId = collectionId,
                query = "%$query%",
                sortBy = sortOption.order.toLong(),
                limit = limit,
                offset = offset,
                mapper = ::mapToArtistListItemModel,
            )
        },
    )

    private fun getCountOfArtistsByCollectionQuery(
        collectionId: String,
        query: String,
    ): Query<Long> = transacter.getNumberOfArtistsByCollection(
        collectionId = collectionId,
        query = "%$query%",
    )
}
