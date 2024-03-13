package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupScaffoldModel
import ly.david.ui.common.relation.RelationsUiState
import ly.david.ui.common.release.ReleasesByEntityUiState

@Stable
internal data class ReleaseGroupUiState(
    val title: String,
    val subtitle: String,
    val isError: Boolean,
    val releaseGroup: ReleaseGroupScaffoldModel?,
    val imageUrl: String,
    val tabs: List<ReleaseGroupTab>,
    val selectedTab: ReleaseGroupTab,
    val query: String,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val relationsUiState: RelationsUiState,
    val eventSink: (ReleaseGroupUiEvent) -> Unit,
) : CircuitUiState
