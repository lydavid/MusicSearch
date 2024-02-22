package ly.david.ui.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.models.history.HistorySortOption
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.domain.history.usecase.GetPagedHistory

class HistoryPresenter(
    private val screen: HistoryScreen,
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val getPagedHistory: GetPagedHistory,
) : Presenter<HistoryScreen.UiState> {
    @Composable
    override fun present(): HistoryScreen.UiState {
        val scope = rememberCoroutineScope()
        var query by rememberSaveable { mutableStateOf("") }
        val sortOption by appPreferences.historySortOption.collectAsState(HistorySortOption.RECENTLY_VISITED)
//            .stateIn(
//                scope = scope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                initialValue = HistorySortOption.RECENTLY_VISITED,
//            )
        val lazyPagingItems: LazyPagingItems<ListItemModel> = getPagedHistory(
            query = query,
            sortOption = sortOption,
        ).collectAsLazyPagingItems()

        fun eventSink(event: HistoryScreen.UiEvent) {
            when (event) {
                is HistoryScreen.UiEvent.UpdateQuery -> {
                    query = event.query
                }

                is HistoryScreen.UiEvent.UpdateSortOption -> {
                    appPreferences.setHistorySortOption(event.sortOption)
                }
            }
        }

        return HistoryScreen.UiState(
            query = query,
            sortOption = sortOption,
            lazyPagingItems = lazyPagingItems,
            eventSink = ::eventSink,
        )
    }
}

//class HistoryViewModel(
//    private val appPreferences: AppPreferences,
//    private val getPagedHistory: GetPagedHistory,
//) : ViewModel() {
//
//    data class ViewModelState(
//        val query: String,
//        val sortOption: HistorySortOption,
//    )
//
//    private val query: MutableStateFlow<String> = MutableStateFlow("")
//    val sortOption: StateFlow<HistorySortOption> = appPreferences.historySortOption
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = HistorySortOption.RECENTLY_VISITED,
//        )
//    private val viewModelState = combine(
//        query,
//        sortOption,
//    ) { query, sortOption ->
//        ViewModelState(
//            query,
//            sortOption,
//        )
//    }
//
//    fun updateQuery(query: String) {
//        this.query.value = query
//    }
//
//    fun updateSortOption(sortOption: HistorySortOption) {
//        appPreferences.setHistorySortOption(sortOption)
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    val lookUpHistory: Flow<PagingData<ListItemModel>> =
//        viewModelState.flatMapLatest { (query, sortOption) ->
//            getPagedHistory(
//                query,
//                sortOption,
//            )
//        }
//            .distinctUntilChanged()
//            .cachedIn(viewModelScope)
//}
