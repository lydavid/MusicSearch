package ly.david.musicsearch.shared.feature.details.label

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface LabelUiEvent : CircuitUiEvent {
    data object NavigateUp : LabelUiEvent
    data object ForceRefresh : LabelUiEvent
    data class UpdateTab(val tab: LabelTab) : LabelUiEvent
    data class UpdateQuery(val query: String) : LabelUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : LabelUiEvent
}
