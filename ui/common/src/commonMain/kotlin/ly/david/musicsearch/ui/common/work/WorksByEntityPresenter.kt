package ly.david.musicsearch.ui.common.work

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.WorkListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.work.usecase.GetWorksByEntity

class WorksByEntityPresenter(
    private val getWorksByEntity: GetWorksByEntity,
) : Presenter<WorksByEntityUiState> {
    @Composable
    override fun present(): WorksByEntityUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var workListItems: Flow<PagingData<WorkListItemModel>> by remember { mutableStateOf(emptyFlow()) }

        LaunchedEffect(
            key1 = id,
            key2 = entity,
            key3 = query,
        ) {
            if (id.isEmpty()) return@LaunchedEffect
            val capturedEntity = entity ?: return@LaunchedEffect

            workListItems = getWorksByEntity(
                entityId = id,
                entity = capturedEntity,
                listFilters = ListFilters(
                    query = query,
                    isRemote = isRemote,
                ),
            )
        }

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
    val eventSink: (WorksByEntityUiEvent) -> Unit,
) : CircuitUiState
