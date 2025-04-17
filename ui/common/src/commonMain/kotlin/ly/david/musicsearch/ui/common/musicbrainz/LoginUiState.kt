package ly.david.musicsearch.ui.common.musicbrainz

import com.slack.circuit.runtime.CircuitUiState

data class LoginUiState(
    val showDialog: Boolean = false,
    val errorMessage: String? = null,
    val eventSink: (LoginUiEvent) -> Unit = {},
) : CircuitUiState
