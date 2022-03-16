package ly.david.mbjc.ui.artist

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import ly.david.mbjc.data.network.BROWSE_LIMIT
import ly.david.mbjc.data.network.DELAY_PAGED_API_CALLS_MS
import ly.david.mbjc.data.persistence.RoomData

// TODO: update description on what it actual does
/**
 * Gets data from MusicBrainz server, stores it in local Room database, then fetches it from Room database.
 * It can determine whether there is anymore data to fetch from MusicBrainz based on how many rows are in our
 * local database.
 *
 * Uses:
 *   * release groups by artist
 *   * releases by release group
 *   * tracks by release -> might be able to get all tracks from browse
 *
 *
 *
 * @param getRemoteResourceCount Computes total number of this resource in MusicBrainz's server.
 *  If null, then that means we don't know yet.
 * @param getLocalResourceCount Computes total number of this resource in our local database.
 * @param browseResource Send browse request for resource with given offset.
 *  Expects back the number of returned resources.
 */
@OptIn(ExperimentalPagingApi::class)
class RoomDataRemoteMediator<RD: RoomData>(
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

            MediatorResult.Success(endOfPaginationReached = browseResource(nextOffset) < BROWSE_LIMIT)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
