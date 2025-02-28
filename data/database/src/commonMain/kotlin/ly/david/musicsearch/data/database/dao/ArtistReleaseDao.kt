package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseListItemModel
import lydavidmusicsearchdatadatabase.Artist_release

class ArtistReleaseDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.artist_releaseQueries

    fun insert(
        artistId: String,
        releaseId: String,
    ) {
        transacter.insert(
            Artist_release(
                artist_id = artistId,
                release_id = releaseId,
            ),
        )
    }

    fun insertAll(
        artistId: String,
        releaseIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            releaseIds.forEach { releaseId ->
                insert(
                    artistId = artistId,
                    releaseId = releaseId,
                )
            }
            releaseIds.size
        }
    }

    fun deleteReleasesByArtist(artistId: String) {
        withTransaction {
            transacter.deleteReleasesByArtist(artistId)
            transacter.deleteReleasesByArtist(artistId)
        }
    }

    fun getNumberOfReleasesByArtist(artistId: String): Flow<Int> =
        transacter.getNumberOfReleasesByArtist(
            artistId = artistId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

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
