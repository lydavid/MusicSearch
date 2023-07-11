package ly.david.data.domain.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import java.io.IOException
import ly.david.data.room.RoomModel
import retrofit2.HttpException

/**
 * When using [LoadType.REFRESH], [hasEntityBeenStored] does not need to be checked.
 * A refresh load will always call [lookupEntity] with force refresh flag.
 */
@OptIn(ExperimentalPagingApi::class)
class LookupEntityRemoteMediator<RM : RoomModel>(
    private val hasEntityBeenStored: suspend () -> Boolean,
    private val lookupEntity: suspend (forceRefresh: Boolean) -> Unit,
    private val deleteLocalEntity: suspend () -> Unit,
) : RemoteMediator<Int, RM>() {

    override suspend fun initialize(): InitializeAction {
        return if (hasEntityBeenStored()) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RM>,
    ): MediatorResult {
        return try {
            if (!hasEntityBeenStored()) {
                lookupEntity(true)
            } else if (loadType == LoadType.REFRESH) {
                deleteLocalEntity()
                lookupEntity(true)
            } else {
                lookupEntity(false)
            }

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (ex: HttpException) {
            MediatorResult.Error(ex)
        } catch (ex: IOException) {
            MediatorResult.Error(ex)
        }
    }
}
