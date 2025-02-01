package ly.david.musicsearch.shared.feature.search

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.history.usecase.DeleteSearchHistory
import ly.david.musicsearch.shared.domain.search.history.usecase.GetSearchHistory
import ly.david.musicsearch.shared.domain.search.history.usecase.RecordSearchHistory
import ly.david.musicsearch.shared.domain.search.results.usecase.GetSearchResults
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.SearchScreen

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

        val searchResults by rememberRetained(query, entity) {
            mutableStateOf(
                getSearchResults(
                    entity = entity,
                    query = query,
                ),
            )
        }

        val searchResultsListState = rememberLazyListState()
        val searchHistory by rememberRetained(query, entity) {
            mutableStateOf(
                if (query.isBlank()) {
                    getSearchHistory(
                        entity = entity,
                    )
                } else {
                    emptyFlow()
                },
            )
        }
        val searchHistoryListState = rememberLazyListState()

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

@Stable
internal data class SearchUiState(
    val query: String,
    val entity: MusicBrainzEntity,
    val searchResults: LazyPagingItems<ListItemModel>,
    val searchResultsListState: LazyListState = LazyListState(),
    val searchHistory: LazyPagingItems<ListItemModel>,
    val searchHistoryListState: LazyListState = LazyListState(),
    val eventSink: (SearchUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface SearchUiEvent : CircuitUiEvent {
    data class UpdateEntity(val entity: MusicBrainzEntity) : SearchUiEvent
    data class UpdateQuery(val query: String) : SearchUiEvent
    data object RecordSearch : SearchUiEvent
    data class DeleteSearchHistory(val item: SearchHistoryListItemModel) : SearchUiEvent
    data object DeleteAllEntitySearchHistory : SearchUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : SearchUiEvent
}
