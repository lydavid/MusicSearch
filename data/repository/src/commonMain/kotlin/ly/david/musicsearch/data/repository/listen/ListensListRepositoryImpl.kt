package ly.david.musicsearch.data.repository.listen

import androidx.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListensListRepository

class ListensListRepositoryImpl(
    private val listenDao: ListenDao,
    private val listenBrainzApi: ListenBrainzApi,
) : ListensListRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun observeListens(
        username: String,
        query: String,
    ): Flow<PagingData<ListenListItemModel>> {
        return if (username.isEmpty()) {
            emptyFlow()
        } else {
            Pager(
                config = PagingConfig(
                    pageSize = 100,
                    initialLoadSize = 100,
                    prefetchDistance = 100,
                ),
                remoteMediator = ListenRemoteMediator(
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

    override fun observeUnfilteredCountOfListensByUser(username: String): Flow<Long?> {
        return listenDao.observeUnfilteredCountOfListensByUser(username = username)
    }
}
