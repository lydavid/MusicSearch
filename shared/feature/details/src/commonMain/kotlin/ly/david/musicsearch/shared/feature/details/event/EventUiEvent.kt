package ly.david.musicsearch.shared.feature.details.event

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface EventUiEvent : CircuitUiEvent {
    data object NavigateUp : EventUiEvent
    data object ForceRefresh : EventUiEvent
    data class UpdateTab(val tab: EventTab) : EventUiEvent
    data class UpdateQuery(val query: String) : EventUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : EventUiEvent
}
