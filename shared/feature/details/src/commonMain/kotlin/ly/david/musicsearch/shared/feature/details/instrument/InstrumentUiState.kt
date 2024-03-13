package ly.david.musicsearch.shared.feature.details.instrument

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.instrument.InstrumentScaffoldModel
import ly.david.ui.common.relation.RelationsUiState

@Stable
internal data class InstrumentUiState(
    val title: String,
    val isError: Boolean,
    val instrument: InstrumentScaffoldModel?,
    val tabs: List<InstrumentTab>,
    val selectedTab: InstrumentTab,
    val query: String,
    val relationsUiState: RelationsUiState,
    val eventSink: (InstrumentUiEvent) -> Unit,
) : CircuitUiState
