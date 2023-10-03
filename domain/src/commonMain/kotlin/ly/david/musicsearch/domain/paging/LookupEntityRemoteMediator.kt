package ly.david.musicsearch.domain.paging

import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import ly.david.data.common.network.RecoverableNetworkException

/**
 * When using [LoadType.REFRESH], [hasEntityBeenStored] does not need to be checked.
 * A refresh load will always call [lookupEntity] with force refresh flag.
 */
@OptIn(ExperimentalPagingApi::class)
class LookupEntityRemoteMediator<DM : Any>(
    private val hasEntityBeenStored: suspend () -> Boolean,
    private val lookupEntity: suspend (forceRefresh: Boolean) -> Unit,
    private val deleteLocalEntity: suspend () -> Unit,
) : RemoteMediator<Int, DM>() {

    override suspend fun initialize(): InitializeAction {
        return if (hasEntityBeenStored()) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DM>,
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
        } catch (ex: RecoverableNetworkException) {
            MediatorResult.Error(ex)
        }
    }
}
