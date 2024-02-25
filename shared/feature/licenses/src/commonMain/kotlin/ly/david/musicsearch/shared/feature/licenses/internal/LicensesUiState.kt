package ly.david.musicsearch.shared.feature.licenses.internal

import com.slack.circuit.runtime.CircuitUiState

internal data class LicensesUiState(
    val eventSink: (LicensesUiEvent) -> Unit,
) : CircuitUiState
