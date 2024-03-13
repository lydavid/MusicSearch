package ly.david.musicsearch.shared.feature.details.releasegroup

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface ReleaseGroupUiEvent : CircuitUiEvent {
    data object NavigateUp : ReleaseGroupUiEvent
    data object ForceRefresh : ReleaseGroupUiEvent
    data class UpdateTab(val tab: ReleaseGroupTab) : ReleaseGroupUiEvent
    data class UpdateQuery(val query: String) : ReleaseGroupUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : ReleaseGroupUiEvent
}
