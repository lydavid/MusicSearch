package ly.david.data.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import java.io.IOException
import ly.david.data.room.RoomModel
import retrofit2.HttpException

/**
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
        return if (hasResourceBeenStored()) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RM>
    ): MediatorResult {

        return try {
            if (!hasResourceBeenStored()) {
                lookupResource(true)
            } else if (loadType == LoadType.REFRESH) {
                deleteLocalResource()
                lookupResource(true)
            } else {
                lookupResource(false)
            }

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (ex: HttpException) {
            MediatorResult.Error(ex)
        } catch (ex: IOException) {
            MediatorResult.Error(ex)
        }
    }
}
