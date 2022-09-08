package ly.david.mbjc.ui.common.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import ly.david.mbjc.data.persistence.RoomModel

@OptIn(ExperimentalPagingApi::class)
internal class LookupRelationsRemoteMediator<RM : RoomModel>(
    private val hasRelationsBeenStored: suspend () -> Boolean,
//    private val deleteLocalResource: suspend () -> Unit,
    private val lookupRelations: suspend () -> Unit
) : RemoteMediator<Int, RM>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RM>
    ): MediatorResult {

        return try {
            if (!hasRelationsBeenStored()) {
                lookupRelations()
            }

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
