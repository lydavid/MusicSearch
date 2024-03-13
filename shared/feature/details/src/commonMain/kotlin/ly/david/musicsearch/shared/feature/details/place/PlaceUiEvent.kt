package ly.david.musicsearch.shared.feature.details.place

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface PlaceUiEvent : CircuitUiEvent {
    data object NavigateUp : PlaceUiEvent
    data object ForceRefresh : PlaceUiEvent
    data class UpdateTab(val tab: PlaceTab) : PlaceUiEvent
    data class UpdateQuery(val query: String) : PlaceUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : PlaceUiEvent
}
