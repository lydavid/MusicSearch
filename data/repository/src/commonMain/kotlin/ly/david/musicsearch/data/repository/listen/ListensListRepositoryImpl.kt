package ly.david.musicsearch.data.repository.listen

import androidx.paging.ExperimentalPagingApi
import androidx.paging.TerminalSeparatorType
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.common.getDateFormatted
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listitem.ListSeparator

class ListensListRepositoryImpl(
    private val listenDao: ListenDao,
    private val listenBrainzApi: ListenBrainzApi,
    private val coroutineScope: CoroutineScope,
) : ListensListRepository {
    override fun observeListens(
        username: String,
        query: String,
        reachedLatest: Boolean,
        reachedOldest: Boolean,
        onReachedLatest: (Boolean) -> Unit,
        onReachedOldest: (Boolean) -> Unit,
    ): Flow<PagingData<Identifiable>> {
        return if (username.isEmpty()) {
            emptyFlow()
        } else {
            pagerFlow(
                username = username,
                query = query,
                reachedLatest = reachedLatest,
                reachedOldest = reachedOldest,
                onReachedLatest = onReachedLatest,
                onReachedOldest = onReachedOldest,
            )
        }
            .distinctUntilChanged()
            .cachedIn(scope = coroutineScope)
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun pagerFlow(
        username: String,
        query: String,
        reachedLatest: Boolean,
        reachedOldest: Boolean,
        onReachedLatest: (Boolean) -> Unit,
        onReachedOldest: (Boolean) -> Unit,
    ): Flow<PagingData<Identifiable>> {
        return Pager(
            config = PagingConfig(
                pageSize = 100,
                initialLoadSize = 100,
                prefetchDistance = 100,
            ),
            remoteMediator = ListenRemoteMediator(
                username = username,
                listenDao = listenDao,
                listenBrainzApi = listenBrainzApi,
                reachedLatest = reachedLatest,
                reachedOldest = reachedOldest,
                onReachedLatest = onReachedLatest,
                onReachedOldest = onReachedOldest,
            ),
            pagingSourceFactory = {
                listenDao.getListensByUser(
                    username = username,
                    query = query,
                )
            },
        ).flow.map { pagingData ->
            pagingData.insertSeparators(terminalSeparatorType = TerminalSeparatorType.SOURCE_COMPLETE) {
                    before: ListenListItemModel?,
                    after: ListenListItemModel?,
                ->
                getListSeparator(
                    before = before,
                    after = after,
                )
            }
        }
    }

    private fun getListSeparator(
        before: ListenListItemModel?,
        after: ListenListItemModel?,
    ): ListSeparator? {
        if (after == null) return null

        val beforeDate = before?.listenedAt?.getDateFormatted()
        val afterDate = after.listenedAt.getDateFormatted()

        if (beforeDate == afterDate) return null

        return ListSeparator(
            id = after.listenedAt.epochSeconds.toString(),
            text = afterDate,
        )
    }

    override fun observeUnfilteredCountOfListensByUser(username: String): Flow<Long?> {
        return listenDao.observeUnfilteredCountOfListensByUser(username = username)
    }
}
