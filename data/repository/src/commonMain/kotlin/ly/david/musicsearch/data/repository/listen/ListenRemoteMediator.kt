package ly.david.musicsearch.data.repository.listen

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType.APPEND
import androidx.paging.LoadType.PREPEND
import androidx.paging.LoadType.REFRESH
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
    private val reachedLatest: Boolean,
    private val reachedOldest: Boolean,
    private val onReachedLatest: (Boolean) -> Unit,
    private val onReachedOldest: (Boolean) -> Unit,
) : RemoteMediator<Int, ListenListItemModel>() {

    override suspend fun initialize(): InitializeAction {
        val listensHaveBeenStoredForUser = listenDao.getOldestTimestampMsByUser(username = username) == null
        return if (listensHaveBeenStoredForUser) {
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
                REFRESH -> {
                    onReachedLatest(false)
                    onReachedOldest(false)
                    listenDao.deleteListensByUser(username = username)
                    append()
                }

                PREPEND -> {
                    if (reachedLatest) {
                        MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        prepend()
                    }
                }

                APPEND -> {
                    if (reachedOldest) {
                        MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        append()
                    }
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

        val newOldestTimeStampS: Long = listensResponse.payload.listens.lastOrNull()?.listenedAtS
            ?: listensResponse.payload.oldest_listen_ts

        val endReached = currentOldestTimeStampS == newOldestTimeStampS
        if (endReached) {
            onReachedOldest(true)
        }

        // Note that we will constantly attempt to find older listens whenever the user changes their query
        // and scrolls to the bottom. These calls are small and fast, so it is okay for now.
        return MediatorResult.Success(
            endOfPaginationReached = currentOldestTimeStampS == newOldestTimeStampS,
        )
    }

    private suspend fun prepend(): MediatorResult.Success {
        val currentLatestTimeStampS = listenDao.getLatestTimestampMsByUser(
            username = username,
        )?.let { it / MS_IN_SECOND }
        val listensResponse = listenBrainzApi.getListensByUser(
            username = username,
            minTimestamp = currentLatestTimeStampS,
        )

        listenDao.insert(listens = listensResponse.asListOfListens())

        val newLatestTimeStampS: Long = listensResponse.payload.listens.firstOrNull()?.listenedAtS
            ?: listensResponse.payload.latest_listen_ts

        val endReached = currentLatestTimeStampS == newLatestTimeStampS
        if (endReached) {
            onReachedLatest(true)
        }

        // We also constantly try to fetch new listens when the user changes their query.
        // Any way to not do that?
        return MediatorResult.Success(
            endOfPaginationReached = currentLatestTimeStampS == newLatestTimeStampS,
        )
    }
}
