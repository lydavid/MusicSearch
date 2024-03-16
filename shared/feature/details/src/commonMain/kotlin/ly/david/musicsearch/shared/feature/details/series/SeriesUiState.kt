package ly.david.musicsearch.shared.feature.details.series

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.series.SeriesScaffoldModel
import ly.david.ui.common.relation.RelationsUiState

@Stable
internal data class SeriesUiState(
    val title: String,
    val isError: Boolean,
    val series: SeriesScaffoldModel?,
    val tabs: List<SeriesTab>,
    val selectedTab: SeriesTab,
    val query: String,
    val relationsUiState: RelationsUiState,
    val eventSink: (SeriesUiEvent) -> Unit,
) : CircuitUiState
