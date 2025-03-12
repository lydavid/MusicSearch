package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToArtistListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import lydavidmusicsearchdatadatabase.Artist
import lydavidmusicsearchdatadatabase.Artists_by_entity

class ArtistDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.artistQueries

    private fun ArtistMusicBrainzModel.toDatabaseModel() = Artist(
        id = id,
        name = name,
        sort_name = sortName,
        disambiguation = disambiguation,
        type = type,
        type_id = typeId,
        gender = gender,
        ipis = ipis,
        isnis = isnis,
        country_code = countryCode,
        begin = lifeSpan?.begin,
        end = lifeSpan?.end,
        ended = lifeSpan?.ended,
        area_id = area?.id,
    )

    /**
     * Appropriate for details screen, where we expect to have more data than previously found browsing.
     */
    fun insertReplace(artist: ArtistMusicBrainzModel) {
        transacter.insertOrReplaceArtist(
            artist = artist.toDatabaseModel(),
        )
    }

    fun insertAll(artists: List<ArtistMusicBrainzModel>) {
        transacter.transaction {
            artists.forEach { artist ->
                transacter.insertOrIgnoreArtist(
                    artist = artist.toDatabaseModel(),
                )
            }
        }
    }

    fun getArtistForDetails(artistId: String): ArtistDetailsModel? {
        return transacter.getArtistForDetails(
            artistId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        sortName: String,
        disambiguation: String?,
        type: String?,
        gender: String?,
        ipis: List<String>?,
        isnis: List<String>?,
        begin: String?,
        end: String?,
        ended: Boolean?,
        areaId: String?,
        areaName: String?,
        countryCode: String?,
        visited: Boolean?,
    ): ArtistDetailsModel {
        val area = if (areaId != null && areaName != null) {
            AreaListItemModel(
                id = areaId,
                name = areaName,
                countryCodes = listOfNotNull(countryCode),
                visited = visited == true,
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
            ipis = ipis,
            isnis = isnis,
            lifeSpan = LifeSpanUiModel(
                begin = begin,
                end = end,
                ended = ended,
            ),
            areaListItemModel = area,
        )
    }

    fun delete(artistId: String) {
        transacter.deleteArtist(artistId)
    }

    fun insertArtistsByEntity(
        entityId: String,
        artistIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            artistIds.forEach { artistId ->
                transacter.insertOrIgnoreArtistByEntity(
                    Artists_by_entity(
                        entity_id = entityId,
                        artist_id = artistId,
                    ),
                )
            }
            artistIds.size
        }
    }

    fun deleteArtistsByEntity(entityId: String) {
        transacter.deleteArtistsByEntity(entityId)
    }

    fun observeCountOfArtistsByEntity(entityId: String): Flow<Int> =
        transacter.getNumberOfArtistsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfArtistsByEntity(entityId: String): Int =
        transacter.getNumberOfArtistsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    fun getArtists(
        entityId: String?,
        entity: MusicBrainzEntity?,
        query: String,
    ): PagingSource<Int, ArtistListItemModel> = when {
        entityId == null || entity == null -> {
            getAllArtists(
                query = query,
            )
        }

        entity == MusicBrainzEntity.COLLECTION -> {
            getArtistsByCollection(
                collectionId = entityId,
                query = query,
            )
        }

        else -> {
            getArtistsByEntity(
                entityId = entityId,
                query = query,
            )
        }
    }

    private fun getAllArtists(
        query: String,
    ) = QueryPagingSource(
        countQuery = transacter.getCountOfAllArtists(
            query = "%$query%",
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
        countQuery = transacter.getNumberOfArtistsByEntity(
            entityId = entityId,
            query = "%$query%",
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
