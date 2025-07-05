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
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
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

        return TracksByReleaseUiState(
            pagingDataFlow = tracksListItems,
            lazyListState = lazyListState,
            collapsedMediumIds = collapsedMediumIds,
            eventSink = { event ->
                handleEvent(
                    event = event,
                    onIdChanged = { id = it },
                    onQueryChanged = { query = it },
                    onToggleMedium = { id ->
                        val mediumId = id.toLong()
                        collapsedMediumIds = if (collapsedMediumIds.contains(mediumId)) {
                            collapsedMediumIds.filter { it != mediumId }.toSet()
                        } else {
                            collapsedMediumIds + setOf(mediumId)
                        }
                    },
                )
            },
        )
    }

    private fun handleEvent(
        event: TracksByEntityUiEvent,
        onIdChanged: (String) -> Unit,
        onQueryChanged: (String) -> Unit,
        onToggleMedium: (String) -> Unit = {},
    ) {
        when (event) {
            is TracksByEntityUiEvent.Get -> {
                onIdChanged(event.byEntityId)
            }
            is TracksByEntityUiEvent.UpdateQuery -> {
                onQueryChanged(event.query)
            }
            is TracksByEntityUiEvent.ToggleMedium -> {
                onToggleMedium(event.id)
            }
        }
    }
}

@Stable
data class TracksByReleaseUiState(
    val pagingDataFlow: Flow<PagingData<ListItemModel>> = emptyFlow(),
    val lazyListState: LazyListState = LazyListState(),
    val collapsedMediumIds: Set<Long> = setOf(),
    val eventSink: (TracksByEntityUiEvent) -> Unit = {},
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
