package ly.david.mbjc.ui.common.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import ly.david.mbjc.data.network.DELAY_PAGED_API_CALLS_MS
import ly.david.mbjc.data.network.SEARCH_BROWSE_LIMIT
import ly.david.mbjc.data.persistence.RoomModel

/**
 * Generic RemoteMediator for loading remote data into [RoomModel].
 *
 * @param getRemoteResourceCount Computes total number of this resource in MusicBrainz's server.
 *  If null, then that means we don't know yet.
 * @param getLocalResourceCount Computes total number of this resource in our local database.
 * @param browseResource Send browse request for resource with given offset.
 *  Expects back the number of returned resources.
 */
@OptIn(ExperimentalPagingApi::class)
class RoomDataRemoteMediator<RD : RoomModel>(
    private val getRemoteResourceCount: suspend () -> Int?,
    private val getLocalResourceCount: suspend () -> Int,
    private val browseResource: suspend (offset: Int) -> Int
) : RemoteMediator<Int, RD>() {

    override suspend fun initialize(): InitializeAction {
        return try {
            if (getRemoteResourceCount() == null) {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            } else {
                InitializeAction.SKIP_INITIAL_REFRESH
            }
        } catch (e: Exception) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RD>
    ): MediatorResult {

        return try {

            val nextOffset: Int = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val localResourceCount = getLocalResourceCount()
                    val remoteResourceCount = getRemoteResourceCount()

                    // It should not be possible for the number of release groups in the database to exceed the total
                    // from Music Brainz's database. But if it does, it could cause an infinite loop here.
                    if (localResourceCount == remoteResourceCount) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    delay(DELAY_PAGED_API_CALLS_MS)
                    localResourceCount
                }
            }

            // Assuming all browse uses this limit.
            MediatorResult.Success(endOfPaginationReached = browseResource(nextOffset) < SEARCH_BROWSE_LIMIT)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
