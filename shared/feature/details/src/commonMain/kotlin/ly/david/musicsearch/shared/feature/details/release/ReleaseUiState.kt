package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.release.ReleaseScaffoldModel
import ly.david.ui.common.relation.RelationsUiState
import ly.david.ui.common.track.TracksByReleaseUiState

@Stable
internal data class ReleaseUiState(
    val title: String,
    val subtitle: String,
    val isError: Boolean,
    val release: ReleaseScaffoldModel?,
    val imageUrl: String,
    val tabs: List<ReleaseTab>,
    val selectedTab: ReleaseTab,
    val query: String,
    val relationsUiState: RelationsUiState,
    val tracksByReleaseUiState: TracksByReleaseUiState,
    val eventSink: (ReleaseUiEvent) -> Unit,
) : CircuitUiState
