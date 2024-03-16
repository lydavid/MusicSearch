package ly.david.musicsearch.shared.feature.details.artist

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface ArtistUiEvent : CircuitUiEvent {
    data object NavigateUp : ArtistUiEvent
    data object ForceRefresh : ArtistUiEvent
    data class UpdateTab(val tab: ArtistTab) : ArtistUiEvent
    data class UpdateQuery(val query: String) : ArtistUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : ArtistUiEvent
}
