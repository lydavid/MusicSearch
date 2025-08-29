package ly.david.musicsearch.data.repository.search

import androidx.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import kotlinx.coroutines.delay
import ly.david.musicsearch.data.database.dao.SearchResultDao
import ly.david.musicsearch.data.musicbrainz.DELAY_PAGED_API_CALLS_MS
import ly.david.musicsearch.data.repository.internal.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

/**
 * Mediates search results from network.
 * Compare with [BrowseEntityRemoteMediator] and [LookupEntityRemoteMediator].
 *
 * @param fetchAndStore Fetch from remote with offset and store in database.
 *  Remove all local data if refreshing. We delegate to call site so that it can be executed in a single transaction.
 *  Returns how many entities were fetched.
 */
@ExperimentalPagingApi
internal class SearchMusicBrainzRemoteMediator(
    private val searchResultDao: SearchResultDao,
    private val entity: MusicBrainzEntityType,
    private val query: String,
    private val fetchAndStore: suspend (offset: Int, removeAll: () -> Unit) -> Int,
) : RemoteMediator<Int, ListItemModel>() {

    override suspend fun initialize(): InitializeAction {
        val metadata = searchResultDao.getMetadata()
        return if (metadata == null || metadata.query != query || metadata.entity != entity) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListItemModel>,
    ): MediatorResult {
        return try {
            val nextOffset: Int = when (loadType) {
                LoadType.REFRESH -> {
                    0
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val searchResultMetadata = searchResultDao.getMetadata()
                    val localCount = searchResultMetadata?.localCount ?: 0
                    val remoteCount = searchResultMetadata?.remoteCount

                    if (localCount == remoteCount) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    delay(DELAY_PAGED_API_CALLS_MS)
                    localCount
                }
            }

            val numberOfEntities = fetchAndStore(
                nextOffset,
            ) {
                /* removeAll */
                if (loadType == LoadType.REFRESH) {
                    searchResultDao.removeAll()
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = numberOfEntities == 0,
            )
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }
}
