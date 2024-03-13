package ly.david.musicsearch.shared.feature.details.series

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface SeriesUiEvent : CircuitUiEvent {
    data object NavigateUp : SeriesUiEvent
    data object ForceRefresh : SeriesUiEvent
    data class UpdateTab(val tab: SeriesTab) : SeriesUiEvent
    data class UpdateQuery(val query: String) : SeriesUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : SeriesUiEvent
}
