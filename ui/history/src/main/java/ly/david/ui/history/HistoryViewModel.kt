package ly.david.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.LookupHistoryRoomModel

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val lookupHistoryDao: LookupHistoryDao
) : ViewModel() {

    private val query: MutableStateFlow<String> = MutableStateFlow("")

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val lookUpHistory: Flow<PagingData<LookupHistoryRoomModel>> =
        query.flatMapLatest { query ->
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    // TODO: if we allow different sorting, then it will multiple these queries
                    //  can we sort with Kotlin? since this is a flow, we can't sort it afterwards
                    if (query.isEmpty()) {
                        lookupHistoryDao.getAllLookupHistory()
                    } else {
                        lookupHistoryDao.getAllLookupHistoryFiltered("%$query%")
                    }
                }
            ).flow
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
