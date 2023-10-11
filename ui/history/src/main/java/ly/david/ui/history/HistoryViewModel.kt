package ly.david.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import ly.david.musicsearch.domain.history.GetPagedHistory
import ly.david.musicsearch.domain.history.HistorySortOption
import ly.david.musicsearch.domain.listitem.ListItemModel
import ly.david.ui.settings.AppPreferences

class HistoryViewModel(
    private val appPreferences: AppPreferences,
    private val getPagedHistory: GetPagedHistory,
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
            getPagedHistory(query, sortOption)
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
