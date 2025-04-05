package ly.david.musicsearch.ui.common.recording

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
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.recording.usecase.GetRecordingsByEntity

class RecordingsListPresenter(
    private val getRecordingsByEntity: GetRecordingsByEntity,
) : Presenter<RecordingsListUiState> {
    @Composable
    override fun present(): RecordingsListUiState {
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val recordingListItems: Flow<PagingData<RecordingListItemModel>> by rememberRetained(id, entity, query) {
            mutableStateOf(
                getRecordingsByEntity(
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

        fun eventSink(event: RecordingsListUiEvent) {
            when (event) {
                is RecordingsListUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is RecordingsListUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return RecordingsListUiState(
            lazyPagingItems = recordingListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}

sealed interface RecordingsListUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : RecordingsListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : RecordingsListUiEvent
}

@Stable
data class RecordingsListUiState(
    val lazyPagingItems: LazyPagingItems<RecordingListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val eventSink: (RecordingsListUiEvent) -> Unit,
) : CircuitUiState
