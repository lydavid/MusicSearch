package ly.david.mbjc.ui.common.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.delay
import ly.david.mbjc.data.network.api.DELAY_PAGED_API_CALLS_MS
import ly.david.mbjc.data.network.api.SEARCH_BROWSE_LIMIT
import ly.david.mbjc.data.persistence.RoomModel

/**
 * Generic RemoteMediator for loading remote data into [RoomModel].
 *
 * When using [LoadType.REFRESH], [getRemoteResourceCount] does not need to be checked.
 * A refresh load will always call [browseResource].
 *
 * @param getRemoteResourceCount Computes total number of this resource in MusicBrainz's server.
 *  If null, then we don't know yet.
 * @param getLocalResourceCount Computes total number of this resource in our local database.
 * @param deleteLocalResource Drops the relevant local resources.
 * @param browseResource Send browse request for resource with given offset.
 *  Expects back the number of returned resources.
 */
@OptIn(ExperimentalPagingApi::class)
internal class BrowseResourceRemoteMediator<RM : RoomModel>(
    private val getRemoteResourceCount: suspend () -> Int?,
    private val getLocalResourceCount: suspend () -> Int,
    private val deleteLocalResource: suspend () -> Unit,
    private val browseResource: suspend (offset: Int) -> Int
) : RemoteMediator<Int, RM>() {

    override suspend fun initialize(): InitializeAction {
        return try {
            if (getRemoteResourceCount() == null) {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            } else {
                InitializeAction.SKIP_INITIAL_REFRESH
            }
        } catch (ex: Exception) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RM>
    ): MediatorResult {

        return try {

            val nextOffset: Int = when (loadType) {
                LoadType.REFRESH -> {
                    // TODO: can we somehow support delta refresh? Might need Dropbox Store
                    deleteLocalResource()
                    0
                }
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

            // Assuming all Browse uses this limit.
            MediatorResult.Success(endOfPaginationReached = browseResource(nextOffset) < SEARCH_BROWSE_LIMIT)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }
}
