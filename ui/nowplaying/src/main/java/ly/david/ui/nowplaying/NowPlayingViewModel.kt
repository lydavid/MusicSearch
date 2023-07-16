package ly.david.ui.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.data.common.getDateFormatted
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.listitem.ListSeparator
import ly.david.data.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.data.domain.listitem.toNowPlayingHistoryListItemModel
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.room.history.nowplaying.NowPlayingHistoryDao
import ly.david.data.room.history.nowplaying.NowPlayingHistoryRoomModel

@HiltViewModel
internal class NowPlayingViewModel @Inject constructor(
    private val nowPlayingHistoryDao: NowPlayingHistoryDao,
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
                    nowPlayingHistoryDao.getAllNowPlayingHistory(
                        query = "%$query%",
                    )
                }
            ).flow.map { pagingData ->
                pagingData
                    .map(NowPlayingHistoryRoomModel::toNowPlayingHistoryListItemModel)
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
}
