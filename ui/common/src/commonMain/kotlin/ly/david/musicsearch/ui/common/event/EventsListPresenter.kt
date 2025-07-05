package ly.david.musicsearch.ui.common.event

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
import ly.david.musicsearch.shared.domain.event.EventsListRepository
import ly.david.musicsearch.shared.domain.event.usecase.GetEvents
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

class EventsListPresenter(
    private val getEvents: GetEvents,
    private val eventsListRepository: EventsListRepository,
) : Presenter<EventsListUiState> {
    @Composable
    override fun present(): EventsListUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(query, browseMethod) {
            mutableStateOf(
                getEvents(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val count by eventsListRepository.observeCountOfEvents(
            browseMethod = browseMethod,
        ).collectAsRetainedState(0)
        val lazyListState = rememberLazyListState()

        fun eventSink(event: EventsListUiEvent) {
            when (event) {
                is EventsListUiEvent.Get -> {
                    browseMethod = event.browseMethod
                    isRemote = event.isRemote
                }

                is EventsListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return EventsListUiState(
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
        event: EventsListUiEvent,
        onBrowseMethodChanged: (BrowseMethod) -> Unit,
        onIsRemoteChanged: (Boolean) -> Unit,
        onQueryChanged: (String) -> Unit,
    ) {
        when (event) {
            is EventsListUiEvent.Get -> {
                onBrowseMethodChanged(event.browseMethod)
                onIsRemoteChanged(event.isRemote)
            }
            is EventsListUiEvent.UpdateQuery -> {
                onQueryChanged(event.query)
            }
        }
    }
}

sealed interface EventsListUiEvent : CircuitUiEvent {
    data class Get(
        val browseMethod: BrowseMethod,
        val isRemote: Boolean = true,
    ) : EventsListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : EventsListUiEvent
}

@Stable
data class EventsListUiState(
    val pagingDataFlow: Flow<PagingData<ListItemModel>> = emptyFlow(),
    val count: Int = 0,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (EventsListUiEvent) -> Unit = {},
) : CircuitUiState
