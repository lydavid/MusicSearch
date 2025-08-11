package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class LookupHistoryDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.lookup_historyQueries

    fun upsert(lookupHistory: LookupHistory) {
        lookupHistory.run {
            transacter.upsert(
                mbid = mbid,
                title = title,
                entity = entity,
                numberOfVisits = numberOfVisits,
                lastAccessed = lastAccessed,
                searchHint = searchHint,
                deleted = deleted,
            )
        }
    }

    fun getAllLookupHistory(
        query: String,
        alphabetically: Boolean,
        alphabeticallyReverse: Boolean,
        recentlyVisited: Boolean,
        leastRecentlyVisited: Boolean,
        mostVisited: Boolean,
        leastVisited: Boolean,
    ): PagingSource<Int, LookupHistoryListItemModel> = QueryPagingSource(
        countQuery = transacter.getAllLookupHistoryCount(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllLookupHistory(
                query = query,
                alphabetically = alphabetically,
                alphabeticallyReverse = alphabeticallyReverse,
                recentlyVisited = recentlyVisited,
                leastRecentlyVisited = leastRecentlyVisited,
                mostVisited = mostVisited,
                leastVisited = leastVisited,
                limit = limit,
                offset = offset,
                mapper = ::mapToLookupHistoryListItemModel,
            )
        },
    )

    fun markAsDeleted(
        mbid: String,
        deleted: Boolean,
    ) {
        transacter.markAsDeleted(
            mbid = mbid,
            deleted = deleted,
        )
    }

    fun markAllAsDeleted(deleted: Boolean) {
        transacter.markAllAsDeleted(deleted)
    }

    fun delete(mbid: String) {
        transacter.delete(mbid)
    }

    fun deleteAll() {
        transacter.deleteAll()
    }
}

private fun mapToLookupHistoryListItemModel(
    mbid: String,
    title: String,
    entity: MusicBrainzEntity,
    numberOfVisits: Int,
    lastAccessed: Instant,
    imageUrl: String?,
    imageId: Long?,
) = LookupHistoryListItemModel(
    id = mbid,
    title = title,
    entity = entity,
    numberOfVisits = numberOfVisits,
    lastAccessed = lastAccessed,
    imageUrl = imageUrl,
    imageId = imageId?.let { ImageId(it) },
)
