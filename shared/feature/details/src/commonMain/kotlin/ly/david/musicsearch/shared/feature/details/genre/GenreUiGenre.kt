package ly.david.musicsearch.shared.feature.details.genre

import com.slack.circuit.runtime.CircuitUiEvent

internal sealed interface GenreUiGenre : CircuitUiEvent {
    data object NavigateUp : GenreUiGenre
    data object ForceRefresh : GenreUiGenre
}
