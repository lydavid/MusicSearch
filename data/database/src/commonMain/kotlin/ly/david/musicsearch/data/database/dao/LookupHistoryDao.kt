package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Instant
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.history.LookupHistoryForListItem
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.Database

class LookupHistoryDao(
    database: Database,
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
    ): PagingSource<Int, LookupHistoryForListItem> = QueryPagingSource(
        countQuery = transacter.getAllLookupHistoryCount(
            query = query,
        ),
        transacter = transacter,
        context = Dispatchers.IO,
    ) { limit, offset ->
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
            mapper = ::mapToLookupHistoryForListItem,
        )
    }

    fun markAsDeleted(mbid: String, deleted: Boolean) {
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

private fun mapToLookupHistoryForListItem(
    mbid: String,
    title: String,
    entity: MusicBrainzEntity,
    numberOfVisits: Int,
    lastAccessed: Instant,
    imageUrl: String?,
) = LookupHistoryForListItem(
    mbid = mbid,
    title = title,
    entity = entity,
    numberOfVisits = numberOfVisits,
    lastAccessed = lastAccessed,
    imageUrl = imageUrl,
)
