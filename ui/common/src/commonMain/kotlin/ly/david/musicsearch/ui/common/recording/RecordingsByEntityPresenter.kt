package ly.david.musicsearch.ui.common.recording

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.recording.usecase.GetRecordingsByEntity

class RecordingsByEntityPresenter(
    private val getRecordingsByEntity: GetRecordingsByEntity,
) : Presenter<RecordingsByEntityUiState> {
    @Composable
    override fun present(): RecordingsByEntityUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var recordingListItems: Flow<PagingData<RecordingListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val lazyListState: LazyListState = rememberLazyListState()

        LaunchedEffect(
            key1 = id,
            key2 = entity,
            key3 = query,
        ) {
            if (id.isEmpty()) return@LaunchedEffect
            val capturedEntity = entity ?: return@LaunchedEffect

            recordingListItems = getRecordingsByEntity(
                entityId = id,
                entity = capturedEntity,
                listFilters = ListFilters(
                    query = query,
                    isRemote = isRemote,
                ),
            )
        }

        fun eventSink(event: RecordingsByEntityUiEvent) {
            when (event) {
                is RecordingsByEntityUiEvent.Get -> {
                    id = event.byEntityId
                    entity = event.byEntity
                    isRemote = event.isRemote
                }

                is RecordingsByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return RecordingsByEntityUiState(
            lazyPagingItems = recordingListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            eventSink = ::eventSink,
        )
    }
}
