package ly.david.musicsearch.shared.feature.settings.internal

import com.slack.circuit.runtime.CircuitUiState

internal data class LoginUiState(
    val eventSink: (LoginUiEvent) -> Unit,
) : CircuitUiState
