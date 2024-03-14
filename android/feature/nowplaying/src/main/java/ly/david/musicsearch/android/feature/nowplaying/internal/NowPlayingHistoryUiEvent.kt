package ly.david.musicsearch.android.feature.nowplaying.internal

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

internal sealed interface NowPlayingHistoryUiEvent : CircuitUiEvent {
    data object NavigateUp : NowPlayingHistoryUiEvent
    data class UpdateQuery(val query: String) : NowPlayingHistoryUiEvent
    data class DeleteHistory(val id: String) : NowPlayingHistoryUiEvent
    data class GoToSearch(
        val query: String,
        val entity: MusicBrainzEntity,
    ) : NowPlayingHistoryUiEvent
}
