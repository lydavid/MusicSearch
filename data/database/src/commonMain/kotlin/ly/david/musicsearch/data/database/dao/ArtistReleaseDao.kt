package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import ly.david.data.core.ReleaseForListItem
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseForListItem
import lydavidmusicsearchdatadatabase.Artist_release

class ArtistReleaseDao(
    database: Database,
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
            )
        )
    }

    fun insertAll(
        artistId: String,
        releaseIds: List<String>,
    ) {
        transacter.transaction {
            releaseIds.forEach { releaseId ->
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

    fun getNumberOfReleasesByArtist(artistId: String): Int =
        transacter.getNumberOfReleasesByArtist(
            artistId = artistId,
            query = "%%",
        ).executeAsOne().toInt()

    fun getReleasesByArtist(
        artistId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByArtist(
            artistId = artistId,
            query = query,
        ),
        transacter = transacter,
        context = Dispatchers.IO,
    ) { limit, offset ->
        transacter.getReleasesByArtist(
            artistId = artistId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToReleaseForListItem,
        )
    }
}
