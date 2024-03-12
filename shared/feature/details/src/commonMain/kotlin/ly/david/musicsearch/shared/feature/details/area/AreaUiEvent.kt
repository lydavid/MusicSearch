package ly.david.musicsearch.shared.feature.details.area

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface AreaUiEvent : CircuitUiEvent {
    data object NavigateUp : AreaUiEvent
    data object ForceRefresh : AreaUiEvent
    data class UpdateTab(val tab: AreaTab) : AreaUiEvent
    data class UpdateQuery(val query: String) : AreaUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : AreaUiEvent
}
