package ly.david.musicsearch.ui.common.recording

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

sealed interface RecordingsByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean = true,
    ) : RecordingsByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : RecordingsByEntityUiEvent
}
