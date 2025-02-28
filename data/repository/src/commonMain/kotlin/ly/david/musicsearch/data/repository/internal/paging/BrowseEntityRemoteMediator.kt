package ly.david.musicsearch.data.repository.internal.paging

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import kotlinx.coroutines.delay
import ly.david.musicsearch.data.musicbrainz.DELAY_PAGED_API_CALLS_MS
import ly.david.musicsearch.data.musicbrainz.SEARCH_BROWSE_LIMIT

/**
 * Generic RemoteMediator for loading remote data into a database, then fetching it from the database.
 *
 * When using [LoadType.REFRESH], [getRemoteEntityCount] does not need to be checked.
 * A refresh load will always call [browseLinkedEntitiesAndStore].
 *
 * @param getRemoteEntityCount Computes total number of this entity in MusicBrainz's server.
 *  If null, then we don't know yet.
 * @param getLocalEntityCount Computes total number of this entity in our local database.
 * @param deleteLocalEntity Drops the relevant local entities.
 * @param browseLinkedEntitiesAndStore Send browse request for entity with given offset and insert them.
 *  Expects back the number of entities successfully inserted.
 *  We will stop paging when this is less than the browse limit, which means we are on the last page.
 */
@OptIn(ExperimentalPagingApi::class)
internal class BrowseEntityRemoteMediator<DM : Any>(
    private val getRemoteEntityCount: suspend () -> Int?,
    private val getLocalEntityCount: suspend () -> Int,
    private val deleteLocalEntity: suspend () -> Unit,
    private val browseLinkedEntitiesAndStore: suspend (offset: Int) -> Int,
) : RemoteMediator<Int, DM>() {

    override suspend fun initialize(): InitializeAction {
        return if (getRemoteEntityCount() == null) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DM>,
    ): MediatorResult {
        return try {
            val nextOffset: Int = when (loadType) {
                LoadType.REFRESH -> {
                    // We want to start with LAUNCH_INITIAL_REFRESH but we don't want to delete on this initial refresh
                    if (getRemoteEntityCount() != null) {
                        deleteLocalEntity()
                    }
                    0
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val localEntityCount = getLocalEntityCount()
                    val remoteEntityCount = getRemoteEntityCount()

                    if (remoteEntityCount != null && localEntityCount >= remoteEntityCount) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    delay(DELAY_PAGED_API_CALLS_MS)
                    localEntityCount
                }
            }

            val browseEntityCount = browseLinkedEntitiesAndStore(nextOffset)

            // Assuming all Browse uses this limit.
            MediatorResult.Success(endOfPaginationReached = browseEntityCount < SEARCH_BROWSE_LIMIT)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }
}
