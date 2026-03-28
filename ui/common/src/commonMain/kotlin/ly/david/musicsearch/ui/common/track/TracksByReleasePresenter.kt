package ly.david.musicsearch.ui.common.track

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.SelectableId
import ly.david.musicsearch.shared.domain.release.usecase.GetTrackIdsByRelease
import ly.david.musicsearch.shared.domain.release.usecase.ObserveTracksByRelease

class TracksByReleasePresenter(
    private val observeTracksByRelease: ObserveTracksByRelease,
    private val getTracksIdsByRelease: GetTrackIdsByRelease,
) : Presenter<TracksByReleaseUiState> {
    @Composable
    override fun present(): TracksByReleaseUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var releaseId: String by rememberSaveable { mutableStateOf("") }
        var mostListenedTrackCount: Long by rememberSaveable { mutableLongStateOf(0) }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(releaseId, query) {
            mutableStateOf(
                observeTracksByRelease(
                    releaseId = releaseId,
                    query = query,
                ),
            )
        }
        // TODO: consider changing "select all" for all other list screens to actually select all like tracks
        //  tracks is special in that all are cached at once
        //  For other screens, it's possible new remote data appears as the user scrolls,
        //  but then it would just be similar to now where they can click select all again to pick up the new data.
        val trackIds: List<SelectableId> by rememberRetained(releaseId) {
            mutableStateOf(getTracksIdsByRelease(releaseId))
        }
        val lazyListState: LazyListState = rememberLazyListState()
        var collapsedMediumIds: Set<Long> by rememberSaveable { mutableStateOf(setOf()) }

        fun eventSink(event: TracksByReleaseUiEvent) {
            when (event) {
                is TracksByReleaseUiEvent.Get -> {
                    releaseId = event.byReleaseId
                    mostListenedTrackCount = event.mostListenedTrackCount
                }

                is TracksByReleaseUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is TracksByReleaseUiEvent.ToggleMedium -> {
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
            pagingDataFlow = pagingDataFlow,
            lazyListState = lazyListState,
            collapsedMediumIds = collapsedMediumIds.toPersistentSet(),
            trackIds = trackIds.toPersistentList(),
            mostListenedTrackCount = mostListenedTrackCount,
            eventSink = { event ->
                eventSink(event = event)
            },
        )
    }
}

@Stable
data class TracksByReleaseUiState(
    val pagingDataFlow: Flow<PagingData<ListItemModel>> = emptyFlow(),
    val lazyListState: LazyListState = LazyListState(),
    val collapsedMediumIds: ImmutableSet<Long> = persistentSetOf(),
    val trackIds: ImmutableList<SelectableId> = persistentListOf(),
    val mostListenedTrackCount: Long = 0L,
    val eventSink: (TracksByReleaseUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface TracksByReleaseUiEvent : CircuitUiEvent {
    data class Get(
        val byReleaseId: String,
        val mostListenedTrackCount: Long,
    ) : TracksByReleaseUiEvent

    data class UpdateQuery(
        val query: String,
    ) : TracksByReleaseUiEvent

    data class ToggleMedium(
        val id: String,
    ) : TracksByReleaseUiEvent
}
