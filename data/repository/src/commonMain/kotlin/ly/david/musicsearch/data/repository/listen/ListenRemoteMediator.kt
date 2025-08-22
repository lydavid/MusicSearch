package ly.david.musicsearch.data.repository.listen

import androidx.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.data.listenbrainz.api.asListOfListens
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import kotlin.coroutines.cancellation.CancellationException

@ExperimentalPagingApi
internal class ListenRemoteMediator(
    private val username: String,
    private val listenDao: ListenDao,
    private val listenBrainzApi: ListenBrainzApi,
) : RemoteMediator<Int, ListenListItemModel>() {

    override suspend fun initialize(): InitializeAction {
        val currentOldestTimeStampMs = listenDao.getOldestTimestampMsByUser(username = username)
        return if (currentOldestTimeStampMs == null) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListenListItemModel>,
    ): MediatorResult {
        return try {
            when (loadType) {
                androidx.paging.LoadType.REFRESH -> {
                    listenDao.deleteListensByUser(username = username)

                    append()
                }
                androidx.paging.LoadType.PREPEND -> {
                    val currentLatestTimeStampS = listenDao.getLatestTimestampMsByUser(
                        username = username,
                    )?.let { it / MS_IN_SECOND }
                    val listensResponse = listenBrainzApi.getListensByUser(
                        username = username,
                        minTimestamp = currentLatestTimeStampS,
                    )

                    listenDao.insert(listens = listensResponse.asListOfListens())

                    val newLatestTimeStampS = listensResponse.payload.listens.firstOrNull()?.listenedAtS

                    // We constantly try to fetch new listens when the user changes their query.
                    // Any way to not do that?
                    MediatorResult.Success(
                        endOfPaginationReached = currentLatestTimeStampS == newLatestTimeStampS,
                    )
                }
                androidx.paging.LoadType.APPEND -> {
                    append()
                }
            }
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }

    private suspend fun append(): MediatorResult.Success {
        val currentOldestTimeStampS = listenDao.getOldestTimestampMsByUser(
            username = username,
        )?.let { it / MS_IN_SECOND }
        val listensResponse = listenBrainzApi.getListensByUser(
            username = username,
            maxTimestamp = currentOldestTimeStampS,
        )

        listenDao.insert(listens = listensResponse.asListOfListens())

        val newOldestTimeStampS = listensResponse.payload.listens.lastOrNull()?.listenedAtS

        // Note that we will constantly attempt to find older listens whenever the user changes their query
        // and scrolls to the bottom. These calls are small and fast, so it is okay for now.
        return MediatorResult.Success(
            endOfPaginationReached = currentOldestTimeStampS == newOldestTimeStampS,
        )
    }
}
