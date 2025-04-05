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
import ly.david.musicsearch.shared.domain.work.usecase.GetWorks

class WorksListPresenter(
    private val getWorks: GetWorks,
) : Presenter<WorksListUiState> {
    @Composable
    override fun present(): WorksListUiState {
        var id: String by rememberSaveable { mutableStateOf("") }
        var query by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val workListItems: Flow<PagingData<WorkListItemModel>> by rememberRetained(id, query, entity) {
            mutableStateOf(
                getWorks(
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

        fun eventSink(event: WorksListUiEvent) {
            when (event) {
                is WorksListUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is WorksListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return WorksListUiState(
            lazyPagingItems = workListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface WorksListUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : WorksListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : WorksListUiEvent
}

@Stable
data class WorksListUiState(
    val lazyPagingItems: LazyPagingItems<WorkListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (WorksListUiEvent) -> Unit,
) : CircuitUiState
