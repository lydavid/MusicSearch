package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.area.AreaScaffoldModel
import ly.david.ui.common.place.PlacesByEntityUiState
import ly.david.ui.common.relation.RelationsUiState
import ly.david.ui.common.release.ReleasesByEntityUiState

@Stable
internal data class AreaUiState(
    val title: String,
    val isError: Boolean = false,
    val area: AreaScaffoldModel? = null,
    val tabs: List<AreaTab> = AreaTab.entries,
    val selectedTab: AreaTab = AreaTab.DETAILS,
    val query: String = "",
    val placesByEntityUiState: PlacesByEntityUiState,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val relationsUiState: RelationsUiState,
    val eventSink: (AreaUiEvent) -> Unit = {},
) : CircuitUiState
