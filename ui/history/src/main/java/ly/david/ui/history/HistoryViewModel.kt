package ly.david.ui.history

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
import ly.david.data.domain.listitem.LookupHistoryListItemModel
import ly.david.data.domain.listitem.toLookupHistoryListItemModel
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.LookupHistoryForListItem

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val lookupHistoryDao: LookupHistoryDao
) : ViewModel() {

    private val query: MutableStateFlow<String> = MutableStateFlow("")

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val lookUpHistory: Flow<PagingData<LookupHistoryListItemModel>> =
        query.flatMapLatest { query ->
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    lookupHistoryDao.getAllLookupHistory("%$query%")
                }
            ).flow.map { pagingData ->
                pagingData.map(LookupHistoryForListItem::toLookupHistoryListItemModel)
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
