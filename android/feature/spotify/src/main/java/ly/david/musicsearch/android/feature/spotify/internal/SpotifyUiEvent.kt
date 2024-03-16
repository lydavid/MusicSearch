package ly.david.musicsearch.android.feature.spotify.internal

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface SpotifyUiEvent : CircuitUiEvent {
    data object NavigateUp : SpotifyUiEvent
    data class GoToSearch(
        val query: String,
        val entity: MusicBrainzEntity,
    ) : SpotifyUiEvent
}
