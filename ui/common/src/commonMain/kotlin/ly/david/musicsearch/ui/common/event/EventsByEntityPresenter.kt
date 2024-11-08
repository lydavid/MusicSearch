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
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.event.usecase.GetEventsByEntity
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class EventsByEntityPresenter(
    private val getEventsByEntity: GetEventsByEntity,
) : Presenter<EventsByEntityUiState> {
    @Composable
    override fun present(): EventsByEntityUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val eventListItems: Flow<PagingData<EventListItemModel>> by rememberRetained(query, id, entity) {
            mutableStateOf(
                getEventsByEntity(
                    entityId = id,
                    entity = entity,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val lazyListState = rememberLazyListState()

        fun eventSink(event: EventsByEntityUiEvent) {
            when (event) {
                is EventsByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is EventsByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return EventsByEntityUiState(
            lazyPagingItems = eventListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface EventsByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : EventsByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : EventsByEntityUiEvent
}

@Stable
data class EventsByEntityUiState(
    val lazyPagingItems: LazyPagingItems<EventListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (EventsByEntityUiEvent) -> Unit = {},
) : CircuitUiState
