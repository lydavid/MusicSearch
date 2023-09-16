package ly.david.data.domain.paging

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import kotlinx.coroutines.delay
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.musicbrainz.api.DELAY_PAGED_API_CALLS_MS
import ly.david.data.musicbrainz.api.SEARCH_BROWSE_LIMIT
import ly.david.data.room.RoomModel

/**
 * Generic RemoteMediator for loading remote data into [RoomModel].
 *
 * When using [LoadType.REFRESH], [getRemoteEntityCount] does not need to be checked.
 * A refresh load will always call [browseEntity].
 *
 * @param getRemoteEntityCount Computes total number of this entity in MusicBrainz's server.
 *  If null, then we don't know yet.
 * @param getLocalEntityCount Computes total number of this entity in our local database.
 * @param deleteLocalEntity Drops the relevant local entities.
 * @param browseEntity Send browse request for entity with given offset.
 *  Expects back the number of returned entities.
 */
@OptIn(ExperimentalPagingApi::class)
class BrowseEntityRemoteMediator<RM : RoomModel>(
    private val getRemoteEntityCount: suspend () -> Int?,
    private val getLocalEntityCount: suspend () -> Int,
    private val deleteLocalEntity: suspend () -> Unit,
    private val browseEntity: suspend (offset: Int) -> Int,
) : RemoteMediator<Int, RM>() {

    override suspend fun initialize(): InitializeAction {
        return if (getRemoteEntityCount() == null) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RM>,
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

                    // It should not be possible for the number of release groups in the database to exceed the total
                    // from MusicBrainz's database. But if it does, it could cause an infinite loop here.
                    if (localEntityCount == remoteEntityCount) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    delay(DELAY_PAGED_API_CALLS_MS)
                    localEntityCount
                }
            }

            // Assuming all Browse uses this limit.
            MediatorResult.Success(endOfPaginationReached = browseEntity(nextOffset) < SEARCH_BROWSE_LIMIT)
        } catch (ex: RecoverableNetworkException) {
            MediatorResult.Error(ex)
        }
    }
}
