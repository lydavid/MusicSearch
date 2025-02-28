package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import lydavidmusicsearchdatadatabase.Artist_release

class ArtistReleaseDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.artist_releaseQueries

    @Suppress("SwallowedException")
    fun insert(
        artistId: String,
        releaseId: String,
    ): Int {
        return try {
            transacter.insertOrFail(
                Artist_release(
                    artist_id = artistId,
                    release_id = releaseId,
                ),
            )
            1
        } catch (ex: Exception) {
            0
        }
    }

    fun insertAll(
        artistId: String,
        releaseIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            releaseIds.sumOf { releaseId ->
                insert(
                    artistId = artistId,
                    releaseId = releaseId,
                )
            }
        }
    }

    fun deleteReleasesByArtist(artistId: String) {
        transacter.deleteReleasesByArtist(artistId)
    }

    fun observeCountOfReleasesByArtist(artistId: String): Flow<Int> =
        transacter.getNumberOfReleasesByArtist(
            artistId = artistId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfReleasesByArtist(artistId: String): Int =
        transacter.getNumberOfReleasesByArtist(
            artistId = artistId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    fun getReleasesByArtist(
        artistId: String,
        query: String,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByArtist(
            artistId = artistId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleasesByArtist(
                artistId = artistId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )
}
