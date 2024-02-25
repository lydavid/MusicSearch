package ly.david.musicsearch.shared.feature.licenses

import com.slack.circuit.runtime.CircuitUiState

internal data class LicensesUiState(
    val eventSink: (LicensesUiEvent) -> Unit,
) : CircuitUiState
