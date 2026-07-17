package ly.david.musicsearch.ui.common.release

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentHashMap
import kotlinx.collections.immutable.toPersistentSet
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.release.ObserveCountOfEachStatus
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.release.ReleaseStatusCount
import ly.david.musicsearch.ui.common.screen.ReleaseStatusesScreen

internal class ReleaseStatusesPresenter(
    private val screen: ReleaseStatusesScreen,
    private val appPreferences: AppPreferences,
    private val observeCountOfEachStatus: ObserveCountOfEachStatus,
) : Presenter<ReleaseStatusesUiState> {
    @Composable
    override fun present(): ReleaseStatusesUiState {
        val showReleaseStatuses by appPreferences.showReleaseStatuses.collectAsRetainedState(
            ReleaseStatus.entries,
        )

        val releaseStatusCounts: List<ReleaseStatusCount> by
            observeCountOfEachStatus(browseMethod = screen.browseMethod).collectAsRetainedState(
                listOf(),
            )

        fun eventSink(event: ReleaseStatusesUiEvent) {
            when (event) {
                is ReleaseStatusesUiEvent.UpdateShowReleaseStatus -> {
                    appPreferences.setShowReleaseStatus(event.status)
                }
            }
        }

        val countByStatus: Map<ReleaseStatus, Int> = releaseStatusCounts.associate { it.status to it.count }

        return ReleaseStatusesUiState(
            selectedStatuses = showReleaseStatuses.toPersistentSet(),
            releaseStatusCounts = countByStatus.toPersistentHashMap(),
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class ReleaseStatusesUiState(
    val selectedStatuses: ImmutableSet<ReleaseStatus> = ReleaseStatus.entries.toPersistentSet(),
    val releaseStatusCounts: ImmutableMap<ReleaseStatus, Int> = persistentMapOf(),
    val eventSink: (ReleaseStatusesUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface ReleaseStatusesUiEvent : CircuitUiEvent {
    data class UpdateShowReleaseStatus(
        val status: ReleaseStatus?,
    ) : ReleaseStatusesUiEvent
}
