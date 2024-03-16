package ly.david.musicsearch.shared.feature.details.work

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface WorkUiEvent : CircuitUiEvent {
    data object NavigateUp : WorkUiEvent
    data object ForceRefresh : WorkUiEvent
    data class UpdateTab(val tab: WorkTab) : WorkUiEvent
    data class UpdateQuery(val query: String) : WorkUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : WorkUiEvent
}
