package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.area.AreaScaffoldModel
import ly.david.ui.common.place.PlacesByEntityUiState
import ly.david.ui.common.release.ReleasesByEntityUiState

@Stable
internal data class AreaUiState(
    val title: String,
    val isError: Boolean,
    val area: AreaScaffoldModel?,
    val tabs: List<AreaTab>,
    val selectedTab: AreaTab,
    val query: String,
    val placesByEntityUiState: PlacesByEntityUiState,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val eventSink: (AreaUiEvent) -> Unit,
) : CircuitUiState
