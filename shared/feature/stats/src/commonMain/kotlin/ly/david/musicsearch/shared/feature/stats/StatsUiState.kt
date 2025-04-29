package ly.david.musicsearch.shared.feature.stats

import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.ui.common.topappbar.Tab

internal data class StatsUiState(
    val stats: Stats,
    val tabs: ImmutableList<Tab>,
) : CircuitUiState
