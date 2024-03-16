package ly.david.musicsearch.shared.feature.details.event

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.event.EventScaffoldModel
import ly.david.ui.common.relation.RelationsUiState

@Stable
internal data class EventUiState(
    val title: String,
    val isError: Boolean,
    val event: EventScaffoldModel?,
    val tabs: List<EventTab>,
    val selectedTab: EventTab,
    val query: String,
    val relationsUiState: RelationsUiState,
    val eventSink: (EventUiEvent) -> Unit,
) : CircuitUiState
