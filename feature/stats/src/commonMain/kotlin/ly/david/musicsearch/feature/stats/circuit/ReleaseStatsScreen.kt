package ly.david.musicsearch.feature.stats.circuit

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.feature.stats.Stats
import ly.david.ui.common.topappbar.Tab

@CommonParcelize
data class ReleaseStatsScreen(
    val releaseId: String,
    val tabs: List<Tab>,
) : Screen {
    data class State(
        val stats: Stats,
        val tabs: List<Tab>,
    ) : CircuitUiState
}
