package ly.david.musicsearch.feature.search.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.paging.compose.LazyPagingItems
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.parcelize.CommonParcelize
import ly.david.musicsearch.domain.search.history.usecase.DeleteSearchHistory
import ly.david.musicsearch.domain.search.history.usecase.GetSearchHistory
import ly.david.musicsearch.domain.search.history.usecase.RecordSearchHistory
import ly.david.musicsearch.domain.search.results.usecase.GetSearchResults

@CommonParcelize
data class SearchScreen(
    val query: String? = null,
    val entity: MusicBrainzEntity? = null,
) : Screen {
    data class UiState(
        val query: String,
        val entity: MusicBrainzEntity,
        val searchResults: LazyPagingItems<ListItemModel>,
        val searchHistory: LazyPagingItems<ListItemModel>,
        val eventSink: (UiEvent) -> Unit,
    ) : CircuitUiState

    sealed interface UiEvent : CircuitUiEvent {
        data class UpdateEntity(val entity: MusicBrainzEntity) : UiEvent
        data class UpdateQuery(val query: String) : UiEvent
        data object RecordSearch : UiEvent
        data class DeleteSearchHistory(val item: SearchHistoryListItemModel) : UiEvent
        data object DeleteAllEntitySearchHistory : UiEvent
    }
}

private const val SEARCH_DELAY_MS = 500L

internal class SearchPresenter(
    private val screen: SearchScreen,
    private val navigator: Navigator,
    private val getSearchResults: GetSearchResults,
    private val getSearchHistory: GetSearchHistory,
    private val recordSearchHistory: RecordSearchHistory,
    private val deleteSearchHistory: DeleteSearchHistory,
) : Presenter<SearchScreen.UiState> {

//        @OptIn(
//        ExperimentalCoroutinesApi::class,
//        FlowPreview::class,
//    )
//    val searchResults: Flow<PagingData<ListItemModel>> =
//        viewModelState.filterNot { it.query.isBlank() }
//            .debounce(SEARCH_DELAY_MS)
//            .flatMapLatest { viewModelState ->
//                getSearchResults(
//                    entity = viewModelState.entity,
//                    query = viewModelState.query,
//                )
//            }
//            .distinctUntilChanged()
//            .cachedIn(viewModelScope)

    @Composable
    override fun present(): SearchScreen.UiState {
        var query by rememberSaveable { mutableStateOf(screen.query.orEmpty()) }
        var entity by rememberSaveable { mutableStateOf(screen.entity ?: MusicBrainzEntity.ARTIST) }
        var searchResults by remember { mutableStateOf(emptyFlow<PagingData<ListItemModel>>()) }
        var searchHistory by remember { mutableStateOf(emptyFlow<PagingData<ListItemModel>>()) }

        LaunchedEffect(
            query,
            entity,
        ) {
            if (query.isEmpty()) {
                searchHistory = getSearchHistory(
                    entity = entity,
                )
            } else {
                delay(SEARCH_DELAY_MS)
                searchResults = getSearchResults(
                    entity = entity,
                    query = query,
                )
            }
        }

        fun eventSink(event: SearchScreen.UiEvent) {
            when (event) {
                is SearchScreen.UiEvent.UpdateEntity -> {
                    entity = event.entity
                }

                is SearchScreen.UiEvent.UpdateQuery -> {
                    query = event.query
                }

                is SearchScreen.UiEvent.RecordSearch -> {
                    recordSearchHistory(
                        entity,
                        query,
                    )
                }

                SearchScreen.UiEvent.DeleteAllEntitySearchHistory -> {
                    deleteSearchHistory(
                        entity = entity,
                    )
                }

                is SearchScreen.UiEvent.DeleteSearchHistory -> {
                    deleteSearchHistory(
                        entity = event.item.entity,
                        query = event.item.query,
                    )
                }
            }
        }

        return SearchScreen.UiState(
            query = query,
            entity = entity,
            searchResults = searchResults.collectAsLazyPagingItems(),
            searchHistory = searchHistory.collectAsLazyPagingItems(),
            eventSink = ::eventSink,
        )
    }

//    val searchQuery = MutableStateFlow("")
//    val searchEntity = MutableStateFlow(MusicBrainzEntity.ARTIST)
//    private val viewModelState = combine(
//        searchQuery,
//        searchEntity,
//    ) { query, entity ->
//        ViewModelState(
//            query,
//            entity,
//        )
//    }

//    fun search(
//        query: String? = null,
//        entity: MusicBrainzEntity? = null,
//    ) {
//        if (query != null) {
//            searchQuery.value = query
//        }
//        if (entity != null) {
//            searchEntity.value = entity
//        }
//    }

//    fun clearQuery() {
//        searchQuery.value = ""
//    }

//    fun recordSearchHistory() {
//        val query = searchQuery.value
//        if (query.isBlank()) return
//        val entity = searchEntity.value
//        recordSearchHistory(
//            entity,
//            query,
//        )
//    }
//
//    fun deleteSearchHistoryItem(item: SearchHistoryListItemModel) {
//        deleteSearchHistory(
//            entity = item.entity,
//            query = item.query,
//        )
//    }
//
//    fun deleteAllSearchHistoryForEntity() {
//        deleteSearchHistory(entity = searchEntity.value)
//    }

//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    val searchHistory: Flow<PagingData<ListItemModel>> =
//        viewModelState.filter { it.query.isBlank() }
//            .flatMapLatest { viewModelState ->
//                getSearchHistory(viewModelState.entity)
//            }
//            .distinctUntilChanged()
//            .cachedIn(viewModelScope)
}
