package ly.david.musicsearch.shared.feature.details.recording

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface RecordingUiEvent : CircuitUiEvent {
    data object NavigateUp : RecordingUiEvent
    data object ForceRefresh : RecordingUiEvent
    data class UpdateTab(val tab: RecordingTab) : RecordingUiEvent
    data class UpdateQuery(val query: String) : RecordingUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : RecordingUiEvent
}
