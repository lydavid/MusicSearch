package ly.david.ui.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.data.core.common.getDateFormatted
import ly.david.data.core.history.NowPlayingHistory
import ly.david.musicsearch.domain.listitem.ListItemModel
import ly.david.musicsearch.domain.listitem.ListSeparator
import ly.david.musicsearch.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.musicsearch.domain.listitem.toNowPlayingHistoryListItemModel
import ly.david.musicsearch.domain.nowplaying.NowPlayingHistoryRepository
import ly.david.musicsearch.domain.paging.MusicBrainzPagingConfig
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class NowPlayingViewModel(
    private val nowPlayingHistoryRepository: NowPlayingHistoryRepository,
) : ViewModel() {

    private val query = MutableStateFlow("")

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val nowPlayingHistory: Flow<PagingData<ListItemModel>> =
        query.flatMapLatest { query ->
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    nowPlayingHistoryRepository.getAllNowPlayingHistory(
                        query = "%$query%",
                    )
                }
            ).flow.map { pagingData ->
                pagingData
                    .map(NowPlayingHistory::toNowPlayingHistoryListItemModel)
                    .insertSeparators(generator = ::generator)
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    private fun generator(
        before: NowPlayingHistoryListItemModel?,
        after: NowPlayingHistoryListItemModel?,
    ): ListSeparator? {
        val beforeDate = before?.lastPlayed?.getDateFormatted()
        val afterDate = after?.lastPlayed?.getDateFormatted()
        return if (beforeDate != afterDate && afterDate != null) {
            ListSeparator(
                id = afterDate,
                text = afterDate,
            )
        } else {
            null
        }
    }

    fun delete(id: String) {
        nowPlayingHistoryRepository.delete(raw = id)
    }
}
