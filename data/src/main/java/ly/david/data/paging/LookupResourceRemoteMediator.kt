package ly.david.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import ly.david.data.persistence.RoomModel

/**
 *
 * When using [LoadType.REFRESH], [hasResourceBeenStored] does not need to be checked.
 * A refresh load will always call [lookupResource] with force refresh flag.
 */
@OptIn(ExperimentalPagingApi::class)
class LookupResourceRemoteMediator<RM : RoomModel>(
    private val hasResourceBeenStored: suspend () -> Boolean,
    private val lookupResource: suspend (forceRefresh: Boolean) -> Unit,
    private val deleteLocalResource: suspend () -> Unit
) : RemoteMediator<Int, RM>() {

    override suspend fun initialize(): InitializeAction {
        return try {
            if (hasResourceBeenStored()) {
                InitializeAction.SKIP_INITIAL_REFRESH
            } else {
                InitializeAction.LAUNCH_INITIAL_REFRESH
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
            if (!hasResourceBeenStored()) {
                lookupResource(false)
            } else if (loadType == LoadType.REFRESH) {
                deleteLocalResource()
                lookupResource(true)
            }

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }
}
