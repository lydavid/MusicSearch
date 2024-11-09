package ly.david.musicsearch.ui.common.instrument

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
import ly.david.musicsearch.shared.domain.instrument.usecase.GetInstrumentsByEntity
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class InstrumentsByEntityPresenter(
    private val getInstrumentsByEntity: GetInstrumentsByEntity,
) : Presenter<InstrumentsByEntityUiState> {
    @Composable
    override fun present(): InstrumentsByEntityUiState {
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val instrumentListItems: Flow<PagingData<InstrumentListItemModel>> by rememberRetained(id, entity, query) {
            mutableStateOf(
                getInstrumentsByEntity(
                    entityId = id,
                    entity = entity,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                ),
            )
        }
        val lazyListState: LazyListState = rememberLazyListState()

        fun eventSink(event: InstrumentsByEntityUiEvent) {
            when (event) {
                is InstrumentsByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is InstrumentsByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return InstrumentsByEntityUiState(
            lazyPagingItems = instrumentListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface InstrumentsByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : InstrumentsByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : InstrumentsByEntityUiEvent
}

@Stable
data class InstrumentsByEntityUiState(
    val lazyPagingItems: LazyPagingItems<InstrumentListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (InstrumentsByEntityUiEvent) -> Unit = {},
) : CircuitUiState
