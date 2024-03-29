package ly.david.musicsearch.feature.stats.internal

import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.feature.stats.Stats
import ly.david.ui.common.topappbar.Tab

internal data class StatsUiState(
    val stats: Stats,
    val tabs: ImmutableList<Tab>,
) : CircuitUiState
