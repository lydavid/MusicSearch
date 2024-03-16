package ly.david.musicsearch.shared.feature.details.work

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.work.WorkScaffoldModel
import ly.david.ui.common.recording.RecordingsByEntityUiState
import ly.david.ui.common.relation.RelationsUiState

@Stable
internal data class WorkUiState(
    val title: String,
    val isError: Boolean,
    val work: WorkScaffoldModel?,
    val tabs: List<WorkTab>,
    val selectedTab: WorkTab,
    val query: String,
    val recordingsByEntityUiState: RecordingsByEntityUiState,
    val relationsUiState: RelationsUiState,
    val eventSink: (WorkUiEvent) -> Unit,
) : CircuitUiState
