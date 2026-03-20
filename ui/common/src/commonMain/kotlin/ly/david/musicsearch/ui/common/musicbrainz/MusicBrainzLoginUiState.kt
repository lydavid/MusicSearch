package ly.david.musicsearch.ui.common.musicbrainz

import com.slack.circuit.runtime.CircuitUiState
import kotlin.time.Instant

data class MusicBrainzLoginUiState(
    val showDialog: Boolean = false,
    val successfulLoginAt: Instant? = null,
    val errorMessage: String? = null,
    val eventSink: (MusicBrainzLoginUiEvent) -> Unit = {},
) : CircuitUiState
