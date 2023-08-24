package ly.david.ui.history

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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.data.common.getDateFormatted
import ly.david.data.domain.history.HistorySortOption
import ly.david.data.domain.history.LookupHistoryRepository
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.listitem.ListSeparator
import ly.david.data.domain.listitem.LookupHistoryListItemModel
import ly.david.data.domain.listitem.toLookupHistoryListItemModel
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.room.history.LookupHistoryForListItem

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val lookupHistoryRepository: LookupHistoryRepository,
) : ViewModel() {

    data class UiState(
        val query: String,
        val sortOption: HistorySortOption,
    )

    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val _sortOption: MutableStateFlow<HistorySortOption> = MutableStateFlow(HistorySortOption.RECENTLY_VISITED)
    val sortOption = _sortOption.asStateFlow()
    private val uiState = combine(query, _sortOption) { query, sortOption ->
        UiState(query, sortOption)
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    fun updateSortOption(sortOption: HistorySortOption) {
        this._sortOption.value = sortOption
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val lookUpHistory: Flow<PagingData<ListItemModel>> =
        uiState.flatMapLatest { (query, sort) ->
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    lookupHistoryRepository.getAllLookupHistory(
                        query = query,
                        sort = sort
                    )
                }
            ).flow.map { pagingData ->
                pagingData
                    .map(LookupHistoryForListItem::toLookupHistoryListItemModel)
                    .insertSeparators { before: LookupHistoryListItemModel?, after: LookupHistoryListItemModel? ->
                        val beforeDate = before?.lastAccessed?.getDateFormatted()
                        val afterDate = after?.lastAccessed?.getDateFormatted()
                        if (beforeDate != afterDate && afterDate != null) {
                            ListSeparator(
                                id = afterDate,
                                text = afterDate,
                            )
                        } else {
                            null
                        }
                    }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
