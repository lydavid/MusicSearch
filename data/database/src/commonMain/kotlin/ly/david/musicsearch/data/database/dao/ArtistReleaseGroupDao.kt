package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.core.CoroutineDispatchers
import ly.david.musicsearch.data.core.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.data.core.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseGroupListItemModel
import lydavidmusicsearchdatadatabase.Artist_release_group

class ArtistReleaseGroupDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.artist_release_groupQueries

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

    private fun insert(
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

    fun deleteReleaseGroupsByArtist(artistId: String) {
        transacter.deleteReleaseGroupsByArtist(artistId)
    }

    fun getNumberOfReleaseGroupsByArtist(artistId: String): Flow<Int> =
        transacter.getNumberOfReleaseGroupsByArtist(
            artistId = artistId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfEachAlbumType(artistId: String): Flow<List<ReleaseGroupTypeCount>> =
        transacter.getCountOfEachAlbumType(
            artistId = artistId,
            mapper = { primaryType, secondaryTypes, count ->
                ReleaseGroupTypeCount(
                    primaryType = primaryType,
                    secondaryTypes = secondaryTypes,
                    count = count.toInt(),
                )
            }
        )
            .asFlow()
            .mapToList(coroutineDispatchers.io)

    fun getReleaseGroupsByArtist(
        artistId: String,
        query: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseGroupListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleaseGroupsByArtist(
            artistId = artistId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getReleaseGroupsByArtist(
            artistId = artistId,
            query = query,
            sorted = sorted,
            limit = limit,
            offset = offset,
            mapper = ::mapToReleaseGroupListItemModel,
        )
    }
}
