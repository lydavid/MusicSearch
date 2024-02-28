package ly.david.musicsearch.shared.feature.settings.internal

import com.slack.circuit.runtime.CircuitUiState

internal data class LoginUiState(
    val showDialog: Boolean = false,
    val eventSink: (LoginUiEvent) -> Unit,
) : CircuitUiState
