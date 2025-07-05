package ly.david.musicsearch.ui.common.place

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.place.PlacesListRepository
import ly.david.musicsearch.shared.domain.place.usecase.GetPlaces
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

class PlacesListPresenter(
    private val getPlaces: GetPlaces,
    private val placesListRepository: PlacesListRepository,
) : Presenter<PlacesListUiState> {
    @Composable
    override fun present(): PlacesListUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(query, browseMethod) {
            mutableStateOf(
                getPlaces(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val count by placesListRepository.observeCountOfPlaces(
            browseMethod = browseMethod,
        ).collectAsRetainedState(0)
        val lazyListState: LazyListState = rememberLazyListState()

        fun eventSink(event: PlacesListUiEvent) {
            when (event) {
                is PlacesListUiEvent.Get -> {
                    browseMethod = event.browseMethod
                    isRemote = event.isRemote
                }

                is PlacesListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return PlacesListUiState(
            pagingDataFlow = pagingDataFlow,
            count = count,
            lazyListState = lazyListState,
            eventSink = { event ->
                handleEvent(
                    event = event,
                    onBrowseMethodChanged = { browseMethod = it },
                    onIsRemoteChanged = { isRemote = it },
                    onQueryChanged = { query = it },
                )
            },
        )
    }

    private fun handleEvent(
        event: PlacesListUiEvent,
        onBrowseMethodChanged: (BrowseMethod) -> Unit,
        onIsRemoteChanged: (Boolean) -> Unit,
        onQueryChanged: (String) -> Unit,
    ) {
        when (event) {
            is PlacesListUiEvent.Get -> {
                onBrowseMethodChanged(event.browseMethod)
                onIsRemoteChanged(event.isRemote)
            }
            is PlacesListUiEvent.UpdateQuery -> {
                onQueryChanged(event.query)
            }
        }
    }
}

sealed interface PlacesListUiEvent : CircuitUiEvent {
    data class Get(
        val browseMethod: BrowseMethod,
        val isRemote: Boolean = true,
    ) : PlacesListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : PlacesListUiEvent
}

@Stable
data class PlacesListUiState(
    val pagingDataFlow: Flow<PagingData<ListItemModel>> = emptyFlow(),
    val count: Int = 0,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (PlacesListUiEvent) -> Unit = {},
) : CircuitUiState
