package ly.david.musicsearch.shared.feature.settings.internal

import com.slack.circuit.runtime.CircuitUiEvent

internal sealed interface LoginUiEvent : CircuitUiEvent {
    data object Login : LoginUiEvent
}
