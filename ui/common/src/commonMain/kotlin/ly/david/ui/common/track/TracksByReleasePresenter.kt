package ly.david.ui.common.track

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
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.release.usecase.GetTracksByRelease

class TracksByReleasePresenter(
    private val getTracksByRelease: GetTracksByRelease,
) : Presenter<TracksByReleaseUiState> {
    @Composable
    override fun present(): TracksByReleaseUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var tracksListItems: Flow<PagingData<ListItemModel>> by remember { mutableStateOf(emptyFlow()) }

        LaunchedEffect(
            key1 = id,
            key2 = query,
        ) {
            if (id.isEmpty()) return@LaunchedEffect

            tracksListItems = getTracksByRelease(
                releaseId = id,
                query = query,
            )
        }

        fun eventSink(event: TracksByEntityUiEvent) {
            when (event) {
                is TracksByEntityUiEvent.Get -> {
                    id = event.byEntityId
                }

                is TracksByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return TracksByReleaseUiState(
            lazyPagingItems = tracksListItems.collectAsLazyPagingItems(),
            eventSink = ::eventSink,
        )
    }
}

@Stable
data class TracksByReleaseUiState(
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val eventSink: (TracksByEntityUiEvent) -> Unit,
) : CircuitUiState

sealed interface TracksByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
    ) : TracksByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : TracksByEntityUiEvent
}
