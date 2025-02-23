package ly.david.musicsearch.ui.common.area

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.area.usecase.GetAreasByEntity
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class AreasByEntityPresenter(
    private val getAreasByEntity: GetAreasByEntity,
) : Presenter<AreasByEntityUiState> {
    @Composable
    override fun present(): AreasByEntityUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val pagingDataFlow: Flow<PagingData<AreaListItemModel>> by rememberRetained(query, id, entity) {
            mutableStateOf(
                getAreasByEntity(
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

        fun eventSink(event: AreasByEntityUiEvent) {
            when (event) {
                is AreasByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is AreasByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return AreasByEntityUiState(
            pagingDataFlow = pagingDataFlow,
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface AreasByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : AreasByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : AreasByEntityUiEvent
}

@Stable
data class AreasByEntityUiState(
    val pagingDataFlow: Flow<PagingData<AreaListItemModel>>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (AreasByEntityUiEvent) -> Unit = {},
) : CircuitUiState
