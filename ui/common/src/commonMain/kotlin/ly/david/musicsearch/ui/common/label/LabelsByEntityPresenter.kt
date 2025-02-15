package ly.david.musicsearch.ui.common.label

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
import ly.david.musicsearch.shared.domain.label.usecase.GetLabelsByEntity
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class LabelsByEntityPresenter(
    private val getLabelsByEntity: GetLabelsByEntity,
) : Presenter<LabelsByEntityUiState> {
    @Composable
    override fun present(): LabelsByEntityUiState {
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val labelListItems: Flow<PagingData<LabelListItemModel>> by rememberRetained(id, entity, query) {
            mutableStateOf(
                getLabelsByEntity(
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
