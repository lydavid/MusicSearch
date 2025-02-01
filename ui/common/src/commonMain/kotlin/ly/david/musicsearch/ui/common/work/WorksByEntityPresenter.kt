package ly.david.musicsearch.ui.common.work

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
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.work.usecase.GetWorksByEntity

class WorksByEntityPresenter(
    private val getWorksByEntity: GetWorksByEntity,
) : Presenter<WorksByEntityUiState> {
    @Composable
    override fun present(): WorksByEntityUiState {
        var id: String by rememberSaveable { mutableStateOf("") }
        var query by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val workListItems: Flow<PagingData<WorkListItemModel>> by rememberRetained(id, query, entity) {
            mutableStateOf(
                getWorksByEntity(
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

        fun eventSink(event: WorksByEntityUiEvent) {
            when (event) {
                is WorksByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is WorksByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return WorksByEntityUiState(
            lazyPagingItems = workListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface WorksByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : WorksByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : WorksByEntityUiEvent
}

@Stable
data class WorksByEntityUiState(
    val lazyPagingItems: LazyPagingItems<WorkListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (WorksByEntityUiEvent) -> Unit,
) : CircuitUiState
