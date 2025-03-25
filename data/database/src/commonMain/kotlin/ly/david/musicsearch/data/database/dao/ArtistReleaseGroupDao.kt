package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import lydavidmusicsearchdatadatabase.Release_groups_by_entity

class ArtistReleaseGroupDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.artist_release_groupQueries

    @Suppress("SwallowedException")
    private fun insertReleaseGroupByArtist(
        artistId: String,
        releaseGroupId: String,
    ): Int {
        return try {
            transacter.insertOrFailArtistReleaseGroup(
                Release_groups_by_entity(
                    entity_id = artistId,
                    release_group_id = releaseGroupId,
                ),
            )
            1
        } catch (ex: Exception) {
            0
        }
    }

    fun insertReleaseGroupsByArtist(
        artistId: String,
        releaseGroupIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            releaseGroupIds.sumOf { releaseGroupId ->
                insertReleaseGroupByArtist(
                    releaseGroupId = releaseGroupId,
                    artistId = artistId,
                )
            }
        }
    }

    fun deleteReleaseGroupsByArtist(artistId: String) {
        transacter.deleteReleaseGroupsByArtist(artistId)
    }

    fun observeCountOfReleaseGroupsByArtist(artistId: String): Flow<Int> =
        transacter.getNumberOfReleaseGroupsByArtist(
            artistId = artistId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfReleaseGroupsByArtist(artistId: String): Int =
        transacter.getNumberOfReleaseGroupsByArtist(
            artistId = artistId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    fun getCountOfEachAlbumType(artistId: String): Flow<List<ReleaseGroupTypeCount>> =
        transacter.getCountOfEachAlbumType(
            artistId = artistId,
            mapper = { primaryType, secondaryTypes, count ->
                ReleaseGroupTypeCount(
                    primaryType = primaryType,
                    secondaryTypes = secondaryTypes,
                    count = count.toInt(),
                )
            },
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
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleaseGroupsByArtist(
                artistId = artistId,
                query = "%$query%",
                sorted = sorted,
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseGroupListItemModel,
            )
        },
    )
}
