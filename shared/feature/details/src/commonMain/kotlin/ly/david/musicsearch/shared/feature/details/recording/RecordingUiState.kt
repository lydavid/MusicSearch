package ly.david.musicsearch.shared.feature.details.recording

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.recording.RecordingScaffoldModel
import ly.david.ui.common.relation.RelationsUiState
import ly.david.ui.common.release.ReleasesByEntityUiState

@Stable
internal data class RecordingUiState(
    val title: String,
    val subtitle: String,
    val isError: Boolean,
    val recording: RecordingScaffoldModel?,
    val tabs: List<RecordingTab>,
    val selectedTab: RecordingTab,
    val query: String,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val relationsUiState: RelationsUiState,
    val eventSink: (RecordingUiEvent) -> Unit,
) : CircuitUiState
