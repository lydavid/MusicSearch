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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ly.david.data.common.getDateFormatted
import ly.david.data.domain.history.HistorySortOption
import ly.david.data.domain.history.LookupHistoryRepository
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.listitem.ListSeparator
import ly.david.data.domain.listitem.LookupHistoryListItemModel
import ly.david.data.domain.listitem.toLookupHistoryListItemModel
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.room.history.LookupHistoryForListItem
import ly.david.ui.settings.AppPreferences

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val lookupHistoryRepository: LookupHistoryRepository,
) : ViewModel() {

    data class ViewModelState(
        val query: String,
        val sortOption: HistorySortOption,
    )

    private val query: MutableStateFlow<String> = MutableStateFlow("")
    val sortOption: StateFlow<HistorySortOption> = appPreferences.historySortOption
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistorySortOption.RECENTLY_VISITED,
        )
    private val viewModelState = combine(query, sortOption) { query, sortOption ->
        ViewModelState(query, sortOption)
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    fun updateSortOption(sortOption: HistorySortOption) {
        appPreferences.setHistorySortOption(sortOption)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val lookUpHistory: Flow<PagingData<ListItemModel>> =
        viewModelState.flatMapLatest { (query, sortOption) ->
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    lookupHistoryRepository.getAllLookupHistory(
                        query = query,
                        sortOption = sortOption
                    )
                }
            ).flow.map { pagingData ->
                pagingData
                    .map(LookupHistoryForListItem::toLookupHistoryListItemModel)
                    .insertSeparators {
                            before: LookupHistoryListItemModel?,
                            after: LookupHistoryListItemModel?,
                        ->
                        getListSeparator(
                            before = before,
                            after = after,
                            sortOption = sortOption
                        )
                    }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    private fun getListSeparator(
        before: LookupHistoryListItemModel?,
        after: LookupHistoryListItemModel?,
        sortOption: HistorySortOption,
    ): ListSeparator? {
        if (sortOption != HistorySortOption.RECENTLY_VISITED &&
            sortOption != HistorySortOption.LEAST_RECENTLY_VISITED
        ) {
            return null
        }

        val beforeDate = before?.lastAccessed?.getDateFormatted()
        val afterDate = after?.lastAccessed?.getDateFormatted()
        if (beforeDate == afterDate || afterDate == null) {
            return null
        }

        return ListSeparator(
            id = afterDate,
            text = afterDate,
        )
    }
}
