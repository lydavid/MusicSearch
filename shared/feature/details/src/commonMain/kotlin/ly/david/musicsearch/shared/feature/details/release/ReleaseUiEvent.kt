package ly.david.musicsearch.shared.feature.details.release

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface ReleaseUiEvent : CircuitUiEvent {
    data object NavigateUp : ReleaseUiEvent
    data object ForceRefresh : ReleaseUiEvent
    data class UpdateTab(val tab: ReleaseTab) : ReleaseUiEvent
    data class UpdateQuery(val query: String) : ReleaseUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : ReleaseUiEvent
}
