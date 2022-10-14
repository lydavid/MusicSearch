package ly.david.mbjc.ui.common.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import ly.david.mbjc.data.persistence.RoomModel

/**
 *
 * When using [LoadType.REFRESH], [hasResourceBeenStored] does not need to be checked.
 * A refresh load will always call [lookupResource].
 */
@OptIn(ExperimentalPagingApi::class)
internal class LookupResourceRemoteMediator<RM : RoomModel>(
    private val hasResourceBeenStored: suspend () -> Boolean,
    private val lookupResource: suspend () -> Unit,
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
                lookupResource()
            } else if (loadType == LoadType.REFRESH) {
                // TODO: refactor to make it logically simpler
                //  just have one function which will handle refresh
                //  it would delete/ then lookup
                deleteLocalResource()
                lookupResource()
            }

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }
}
