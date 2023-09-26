package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import ly.david.data.core.release.ReleaseForListItem
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseForListItem
import lydavidmusicsearchdatadatabase.Release_release_group

class ReleaseReleaseGroupDao(
    database: Database,
) : EntityDao {
    override val transacter = database.release_release_groupQueries

    fun insert(
        releaseId: String,
        releaseGroupId: String,
    ) {
        transacter.insert(
            Release_release_group(
                release_id = releaseId,
                release_group_id = releaseGroupId,
            )
        )
    }

    fun insertAll(
        releaseGroupId: String,
        releaseIds: List<String>,
    ) {
        transacter.transaction {
            releaseIds.forEach { releaseId ->
                insert(
                    releaseId = releaseId,
                    releaseGroupId = releaseGroupId,
                )
            }
        }
    }

    fun deleteReleasesByReleaseGroup(releaseGroupId: String) {
        withTransaction {
            transacter.deleteReleasesByReleaseGroup(releaseGroupId)
            transacter.deleteReleaseReleaseGroupLinks(releaseGroupId)
        }
    }

    fun getNumberOfReleasesByReleaseGroup(releaseGroupId: String): Int =
        transacter.getNumberOfReleasesByReleaseGroup(
            releaseGroupId = releaseGroupId,
            query = "%%",
        ).executeAsOne().toInt()

    fun getReleasesByReleaseGroup(
        releaseGroupId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByReleaseGroup(
            releaseGroupId = releaseGroupId,
            query = query,
        ),
        transacter = transacter,
        context = Dispatchers.IO,
    ) { limit, offset ->
        transacter.getReleasesByReleaseGroup(
            releaseGroupId = releaseGroupId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToReleaseForListItem,
        )
    }
}
