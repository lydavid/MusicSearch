package ly.david.mbjc.ui.common.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import ly.david.mbjc.data.persistence.RoomModel

// TODO: can be generalized for tracks
//  this works for anything that is "looked up" in a single call
@OptIn(ExperimentalPagingApi::class)
internal class LookupRelationsRemoteMediator<RM : RoomModel>(
    private val hasRelationsBeenStored: suspend () -> Boolean,
    private val lookupRelations: suspend () -> Unit,
    private val deleteLocalResource: suspend () -> Unit
) : RemoteMediator<Int, RM>() {

    override suspend fun initialize(): InitializeAction {
        return try {
            if (hasRelationsBeenStored()) {
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
            if (!hasRelationsBeenStored()) {
                lookupRelations()
            } else if (loadType == LoadType.REFRESH) {
                deleteLocalResource()
                lookupRelations()
            }

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }
}
