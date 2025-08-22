package ly.david.musicsearch.data.repository.listen

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType.APPEND
import androidx.paging.LoadType.PREPEND
import androidx.paging.LoadType.REFRESH
import app.cash.paging.LoadType
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.data.listenbrainz.api.asListOfListens
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import kotlin.coroutines.cancellation.CancellationException

class ListensListRepositoryImpl(
    private val listenDao: ListenDao,
    private val listenBrainzApi: ListenBrainzApi,
) : ListensListRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun observeListens(
        username: String,
        query: String,
    ): Flow<PagingData<ListenListItemModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 100,
                initialLoadSize = 100,
                prefetchDistance = 100,
            ),
            remoteMediator = SearchMusicBrainzRemoteMediator(
                username = username,
                listenDao = listenDao,
                listenBrainzApi = listenBrainzApi,
            ),
            pagingSourceFactory = {
                listenDao.getListensByUser(
                    username = username,
                    query = query,
                )
            },
        ).flow
    }
}

@ExperimentalPagingApi
internal class SearchMusicBrainzRemoteMediator(
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

    // TODO: delete all on reload for now and when username changes
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListenListItemModel>,
    ): MediatorResult {
        return try {
            when (loadType) {
                REFRESH -> {
                    println("findme: refresh")
                    listenDao.deleteListensByUser(username = username)

                    append()
                }
                PREPEND -> {
                    println("findme: prepend")

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
                APPEND -> {
                    println("findme: append")

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
