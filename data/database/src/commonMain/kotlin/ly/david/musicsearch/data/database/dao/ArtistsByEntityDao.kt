package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToArtistListItemModel
import lydavidmusicsearchdatadatabase.Artists_by_entity

class ArtistsByEntityDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.artists_by_entityQueries

    fun insert(
        entityId: String,
        artistId: String,
    ) {
        transacter.insert(
            Artists_by_entity(
                entity_id = entityId,
                artist_id = artistId,
            ),
        )
    }

    fun insertAll(
        entityAndArtistIds: List<Pair<String, String>>,
    ) {
        transacter.transaction {
            entityAndArtistIds.forEach { (entityId, artistId) ->
                insert(
                    entityId = entityId,
                    artistId = artistId,
                )
            }
        }
    }

    fun deleteArtistsByEntity(entityId: String) {
        transacter.deleteArtistsByEntity(entityId)
    }

    fun getNumberOfArtistsByEntity(entityId: String): Flow<Int> =
        transacter.getNumberOfArtistsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getArtistsByEntity(
        entityId: String,
        query: String,
    ): PagingSource<Int, ArtistListItemModel> = QueryPagingSource(
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
}
