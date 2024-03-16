package ly.david.musicsearch.shared.feature.details.label

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.label.LabelScaffoldModel
import ly.david.ui.common.relation.RelationsUiState
import ly.david.ui.common.release.ReleasesByEntityUiState

@Stable
internal data class LabelUiState(
    val title: String,
    val isError: Boolean,
    val label: LabelScaffoldModel?,
    val tabs: List<LabelTab>,
    val selectedTab: LabelTab,
    val query: String,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val relationsUiState: RelationsUiState,
    val eventSink: (LabelUiEvent) -> Unit,
) : CircuitUiState
