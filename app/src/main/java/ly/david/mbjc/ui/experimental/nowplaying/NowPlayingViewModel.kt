package ly.david.mbjc.ui.experimental.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.data.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.data.domain.listitem.toNowPlayingHistoryListItemModel
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.room.history.nowplaying.NowPlayingHistoryDao

@HiltViewModel
internal class NowPlayingViewModel @Inject constructor(
    private val nowPlayingHistoryDao: NowPlayingHistoryDao,
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val nowPlayingHistory: Flow<PagingData<NowPlayingHistoryListItemModel>> =
        searchQuery
            .flatMapLatest {
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    pagingSourceFactory = {
                        nowPlayingHistoryDao.getAllNowPlayingHistory()
                    }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toNowPlayingHistoryListItemModel()
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
