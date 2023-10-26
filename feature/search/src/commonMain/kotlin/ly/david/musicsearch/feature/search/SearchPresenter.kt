package ly.david.musicsearch.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.delay
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.search.history.usecase.DeleteSearchHistory
import ly.david.musicsearch.domain.search.history.usecase.GetSearchHistory
import ly.david.musicsearch.domain.search.history.usecase.RecordSearchHistory
import ly.david.musicsearch.domain.search.results.usecase.GetSearchResults

data class SearchUiState(
    val query: String = "",
    val results: LazyPagingItems<ListItemModel>,
    val eventSink: (SearchUiEvent) -> Unit,
) : CircuitUiState

sealed interface SearchUiEvent : CircuitUiEvent {
    //    data object ClearQuery: SearchUiEvent
    data class UpdateQuery(val query: String) : SearchUiEvent
    data class UpdateEntity(val entity: MusicBrainzEntity) : SearchUiEvent
}

private const val SEARCH_DELAY_MS = 500L

class SearchPresenter(
    private val getSearchResults: GetSearchResults,
    private val getSearchHistory: GetSearchHistory,
    private val recordSearchHistory: RecordSearchHistory,
    private val deleteSearchHistory: DeleteSearchHistory,
) : Presenter<SearchUiState> {

    @Composable
    override fun present(): SearchUiState {
        var query by remember { mutableStateOf("") }
        var entity by remember { mutableStateOf(MusicBrainzEntity.ARTIST) }

        val results: LazyPagingItems<ListItemModel> = getSearchResults(
            entity = entity,
            query = query,
        ).collectAsLazyPagingItems()

        LaunchedEffect(
            query,
            entity,
        ) {
            delay(SEARCH_DELAY_MS)

//            results
        }

        return SearchUiState(
            query = query,
            results = results,
        ) { event ->
            when (event) {
                is SearchUiEvent.UpdateQuery -> query = event.query
                is SearchUiEvent.UpdateEntity -> entity = event.entity
            }
        }
    }
}

// TODO: https://medium.com/@chrisathanas/how-to-use-parcels-on-kotlin-multiplatform-mobile-kmm-e29590816624
// @Parcelize
// object SearchScreen : Screen
