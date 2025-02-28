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
import lydavidmusicsearchdatadatabase.Release_release_group

class ReleaseReleaseGroupDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.release_release_groupQueries

    @Suppress("SwallowedException")
    fun insert(
        releaseId: String,
        releaseGroupId: String,
    ): Int {
        return try {
            transacter.insertOrFail(
                Release_release_group(
                    release_id = releaseId,
                    release_group_id = releaseGroupId,
                ),
            )
            1
        } catch (ex: Exception) {
            0
        }
    }

    fun insertAll(
        releaseGroupId: String,
        releaseIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            releaseIds.sumOf { releaseId ->
                insert(
                    releaseId = releaseId,
                    releaseGroupId = releaseGroupId,
                )
            }
        }
    }

    fun deleteReleasesByReleaseGroup(releaseGroupId: String) {
        transacter.deleteReleasesByReleaseGroup(releaseGroupId)
    }

    fun deleteReleaseGroupByReleaseLink(
        releaseId: String,
    ) {
        transacter.deleteReleaseReleaseGroupLink(releaseId = releaseId)
    }

    fun observeCountOfReleasesByReleaseGroup(releaseGroupId: String): Flow<Int> =
        transacter.getNumberOfReleasesByReleaseGroup(
            releaseGroupId = releaseGroupId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfReleasesByReleaseGroup(releaseGroupId: String): Int =
        transacter.getNumberOfReleasesByReleaseGroup(
            releaseGroupId = releaseGroupId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    fun getReleasesByReleaseGroup(
        releaseGroupId: String,
        query: String,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByReleaseGroup(
            releaseGroupId = releaseGroupId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleasesByReleaseGroup(
                releaseGroupId = releaseGroupId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )
}
