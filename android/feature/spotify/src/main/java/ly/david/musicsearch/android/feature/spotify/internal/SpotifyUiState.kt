package ly.david.musicsearch.android.feature.spotify.internal

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState

@Stable
internal data class SpotifyUiState(
    val metadata: SpotifyMetadata,
    val eventSink: (SpotifyUiEvent) -> Unit,
) : CircuitUiState
