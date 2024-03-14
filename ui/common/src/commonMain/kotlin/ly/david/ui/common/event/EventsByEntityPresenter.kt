package ly.david.ui.common.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.event.usecase.GetEventsByEntity

class EventsByEntityPresenter(
    private val getEventsByEntity: GetEventsByEntity,
) : Presenter<EventsByEntityUiState> {
    @Composable
    override fun present(): EventsByEntityUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var eventListItems: Flow<PagingData<EventListItemModel>> by remember { mutableStateOf(emptyFlow()) }

        LaunchedEffect(
            key1 = id,
            key2 = entity,
            key3 = query,
        ) {
            if (id.isEmpty()) return@LaunchedEffect
            val capturedEntity = entity ?: return@LaunchedEffect

            eventListItems = getEventsByEntity(
                entityId = id,
                entity = capturedEntity,
                listFilters = ListFilters(
                    query = query,
                ),
            )
        }

        fun eventSink(event: EventsByEntityUiEvent) {
            when (event) {
                is EventsByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                }

                is EventsByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return EventsByEntityUiState(
            lazyPagingItems = eventListItems.collectAsLazyPagingItems(),
            eventSink = ::eventSink,
        )
    }
}