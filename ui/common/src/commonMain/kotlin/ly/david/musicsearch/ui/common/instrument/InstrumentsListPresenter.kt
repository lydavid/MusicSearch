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
import ly.david.musicsearch.shared.domain.instrument.usecase.GetInstruments
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class InstrumentsListPresenter(
    private val getInstruments: GetInstruments,
) : Presenter<InstrumentsListUiState> {
    @Composable
    override fun present(): InstrumentsListUiState {
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val instrumentListItems: Flow<PagingData<InstrumentListItemModel>> by rememberRetained(id, entity, query) {
            mutableStateOf(
                getInstruments(
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

        fun eventSink(event: InstrumentsListUiEvent) {
            when (event) {
                is InstrumentsListUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is InstrumentsListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return InstrumentsListUiState(
            lazyPagingItems = instrumentListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface InstrumentsListUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : InstrumentsListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : InstrumentsListUiEvent
}

@Stable
data class InstrumentsListUiState(
    val lazyPagingItems: LazyPagingItems<InstrumentListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (InstrumentsListUiEvent) -> Unit = {},
) : CircuitUiState
