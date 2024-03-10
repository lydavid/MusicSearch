package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.area.AreaScaffoldModel

@Stable
internal data class AreaUiState(
    val title: String,
    val isError: Boolean,
    val area: AreaScaffoldModel?,
    val tabs: List<AreaTab>,
    val query: String,
    val showMoreInfoInReleaseListItem: Boolean,
    val eventSink: (AreaUiEvent) -> Unit,
) : CircuitUiState
