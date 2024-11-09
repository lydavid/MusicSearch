package ly.david.musicsearch.ui.common.track

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
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.release.usecase.GetTracksByRelease

class TracksByReleasePresenter(
    private val getTracksByRelease: GetTracksByRelease,
) : Presenter<TracksByReleaseUiState> {
    @Composable
    override fun present(): TracksByReleaseUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        val tracksListItems: Flow<PagingData<ListItemModel>> by rememberRetained(id, query) {
            mutableStateOf(
                getTracksByRelease(
                    releaseId = id,
                    query = query,
                ),
            )
        }
        val lazyListState: LazyListState = rememberLazyListState()
        var collapsedMediumIds: Set<Long> by rememberSaveable { mutableStateOf(setOf()) }

        fun eventSink(event: TracksByEntityUiEvent) {
            when (event) {
                is TracksByEntityUiEvent.Get -> {
                    id = event.byEntityId
                }

                is TracksByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is TracksByEntityUiEvent.ToggleMedium -> {
                    val mediumId = event.id.toLong()
                    collapsedMediumIds = if (collapsedMediumIds.contains(mediumId)) {
                        collapsedMediumIds.filter { it != mediumId }.toSet()
                    } else {
                        collapsedMediumIds + setOf(mediumId)
                    }
                }
            }
        }

        return TracksByReleaseUiState(
            lazyPagingItems = tracksListItems.collectAsLazyPagingItems(),
            lazyListState = lazyListState,
            collapsedMediumIds = collapsedMediumIds,
            eventSink = ::eventSink,
        )
    }
}

@Stable
data class TracksByReleaseUiState(
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val lazyListState: LazyListState = LazyListState(),
    val collapsedMediumIds: Set<Long> = setOf(),
    val eventSink: (TracksByEntityUiEvent) -> Unit,
) : CircuitUiState

sealed interface TracksByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
    ) : TracksByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : TracksByEntityUiEvent

    data class ToggleMedium(
        val id: String,
    ) : TracksByEntityUiEvent
}
