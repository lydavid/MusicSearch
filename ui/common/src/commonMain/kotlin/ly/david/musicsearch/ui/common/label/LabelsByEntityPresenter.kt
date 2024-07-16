package ly.david.musicsearch.ui.common.label

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.label.usecase.GetLabelsByEntity

class LabelsByEntityPresenter(
    private val getLabelsByEntity: GetLabelsByEntity,
) : Presenter<LabelsByEntityUiState> {
    @Composable
    override fun present(): LabelsByEntityUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var labelListItems: Flow<PagingData<LabelListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val lazyListState: LazyListState = rememberLazyListState()

        LaunchedEffect(
            key1 = id,
            key2 = entity,
            key3 = query,
        ) {
            if (id.isEmpty()) return@LaunchedEffect
            val capturedEntity = entity ?: return@LaunchedEffect

            labelListItems = getLabelsByEntity(
                entityId = id,
                entity = capturedEntity,
                listFilters = ListFilters(
                    query = query,
                    isRemote = isRemote,
                ),
            )
        }

        fun eventSink(event: LabelsByEntityUiEvent) {
            when (event) {
                is LabelsByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is LabelsByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return LabelsByEntityUiState(
            lazyPagingItems = labelListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface LabelsByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : LabelsByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : LabelsByEntityUiEvent
}

@Stable
data class LabelsByEntityUiState(
    val lazyPagingItems: LazyPagingItems<LabelListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (LabelsByEntityUiEvent) -> Unit = {},
) : CircuitUiState
