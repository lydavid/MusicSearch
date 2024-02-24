package ly.david.musicsearch.shared.feature.search.internal

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
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.search.history.usecase.DeleteSearchHistory
import ly.david.musicsearch.domain.search.history.usecase.GetSearchHistory
import ly.david.musicsearch.domain.search.history.usecase.RecordSearchHistory
import ly.david.musicsearch.domain.search.results.usecase.GetSearchResults
import ly.david.musicsearch.shared.feature.search.SearchScreen
import ly.david.musicsearch.shared.screens.DetailsScreen

private const val SEARCH_DELAY_MS = 500L

internal class SearchPresenter(
    private val screen: SearchScreen,
    private val navigator: Navigator,
    private val getSearchResults: GetSearchResults,
    private val getSearchHistory: GetSearchHistory,
    private val recordSearchHistory: RecordSearchHistory,
    private val deleteSearchHistory: DeleteSearchHistory,
) : Presenter<SearchScreen.UiState> {

    @Composable
    override fun present(): SearchScreen.UiState {
        var query by rememberSaveable { mutableStateOf(screen.query.orEmpty()) }
        var entity by rememberSaveable { mutableStateOf(screen.entity ?: MusicBrainzEntity.ARTIST) }
        var searchResults by remember { mutableStateOf(emptyFlow<PagingData<ListItemModel>>()) }
        var searchHistory by remember { mutableStateOf(emptyFlow<PagingData<ListItemModel>>()) }

        LaunchedEffect(
            key1 = query,
            key2 = entity,
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

                is SearchScreen.UiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                event.entity,
                                event.id,
                                event.title,
                            ),
                        ),
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
}
