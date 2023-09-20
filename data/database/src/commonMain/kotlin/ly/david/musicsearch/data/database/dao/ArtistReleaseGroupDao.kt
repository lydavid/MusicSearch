package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import ly.david.data.core.ReleaseGroupForListItem
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseGroupWithArtistCredits
import lydavidmusicsearchdatadatabase.Artist_release_group

class ArtistReleaseGroupDao(
    database: Database,
) : EntityDao {
    override val transacter = database.artist_release_groupQueries

    fun insert(
        artistId: String,
        releaseGroupId: String,
    ) {
        transacter.insert(
            Artist_release_group(
                artist_id = artistId,
                release_group_id = releaseGroupId,
            )
        )
    }

    fun insertAll(
        artistId: String,
        releaseGroupIds: List<String>,
    ) {
        transacter.transaction {
            releaseGroupIds.forEach { releaseGroupId ->
                insert(
                    releaseGroupId = releaseGroupId,
                    artistId = artistId,
                )
            }
        }
    }

    fun deleteReleaseGroupsByArtist(artistId: String) {
        transacter.deleteReleaseGroupsByArtist(artistId)
    }

    fun getNumberOfReleaseGroupsByArtist(artistId: String): Int =
        transacter.getNumberOfReleaseGroupsByArtist(
            artistId = artistId,
            query = "%%",
        ).executeAsOne().toInt()

    fun getReleaseGroupsByArtist(
        artistId: String,
        query: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseGroupForListItem> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleaseGroupsByArtist(
            artistId = artistId,
            query = query,
        ),
        transacter = transacter,
        context = Dispatchers.IO,
    ) { limit, offset ->
        transacter.getReleaseGroupsByArtist(
            artistId = artistId,
            query = query,
            sorted = sorted,
            limit = limit,
            offset = offset,
            mapper = ::mapToReleaseGroupWithArtistCredits,
        )
    }
}
