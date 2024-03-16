package ly.david.musicsearch.shared.feature.details.place

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.place.PlaceScaffoldModel
import ly.david.ui.common.event.EventsByEntityUiState
import ly.david.ui.common.relation.RelationsUiState

@Stable
internal data class PlaceUiState(
    val title: String,
    val isError: Boolean,
    val place: PlaceScaffoldModel?,
    val tabs: List<PlaceTab>,
    val selectedTab: PlaceTab,
    val query: String,
    val eventsByEntityUiState: EventsByEntityUiState,
    val relationsUiState: RelationsUiState,
    val eventSink: (PlaceUiEvent) -> Unit,
) : CircuitUiState
