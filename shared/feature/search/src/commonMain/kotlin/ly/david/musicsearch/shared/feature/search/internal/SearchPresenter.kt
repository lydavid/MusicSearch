package ly.david.musicsearch.shared.feature.search.internal

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.history.usecase.DeleteSearchHistory
import ly.david.musicsearch.shared.domain.search.history.usecase.GetSearchHistory
import ly.david.musicsearch.shared.domain.search.history.usecase.RecordSearchHistory
import ly.david.musicsearch.shared.domain.search.results.usecase.GetSearchResults
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.SearchScreen

private const val SEARCH_DELAY_MS = 500L

internal class SearchPresenter(
    private val screen: SearchScreen,
    private val navigator: Navigator,
    private val getSearchResults: GetSearchResults,
    private val getSearchHistory: GetSearchHistory,
    private val recordSearchHistory: RecordSearchHistory,
    private val deleteSearchHistory: DeleteSearchHistory,
) : Presenter<SearchUiState> {

    @Composable
    override fun present(): SearchUiState {
        var query by rememberSaveable { mutableStateOf(screen.query.orEmpty()) }
        var entity by rememberSaveable { mutableStateOf(screen.entity ?: MusicBrainzEntity.ARTIST) }
        var searchResults by remember { mutableStateOf(emptyFlow<PagingData<ListItemModel>>()) }
        val searchResultsListState = rememberLazyListState()
        var searchHistory by remember { mutableStateOf(emptyFlow<PagingData<ListItemModel>>()) }
        val searchHistoryListState = rememberLazyListState()

        LaunchedEffect(
            key1 = query,
            key2 = entity,
        ) {
            if (query.isBlank()) {
                searchHistory = getSearchHistory(
                    entity = entity,
                )
            } else {
                delay(SEARCH_DELAY_MS)
                searchResults = getSearchResults(
                    entity = entity,
                    query = query,
                )
                searchResultsListState.scrollToItem(0)
            }
        }

        fun eventSink(event: SearchUiEvent) {
            when (event) {
                is SearchUiEvent.UpdateEntity -> {
                    entity = event.entity
                }

                is SearchUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is SearchUiEvent.RecordSearch -> {
                    recordSearchHistory(
                        entity,
                        query,
                    )
                }

                SearchUiEvent.DeleteAllEntitySearchHistory -> {
                    deleteSearchHistory(
                        entity = entity,
                    )
                }

                is SearchUiEvent.DeleteSearchHistory -> {
                    deleteSearchHistory(
                        entity = event.item.entity,
                        query = event.item.query,
                    )
                }

                is SearchUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                entity = event.entity,
                                id = event.id,
                                title = event.title,
                            ),
                        ),
                    )
                }
            }
        }

        return SearchUiState(
            query = query,
            entity = entity,
            searchResults = searchResults.collectAsLazyPagingItems(),
            searchResultsListState = searchResultsListState,
            searchHistory = searchHistory.collectAsLazyPagingItems(),
            searchHistoryListState = searchHistoryListState,
            eventSink = ::eventSink,
        )
    }
}
