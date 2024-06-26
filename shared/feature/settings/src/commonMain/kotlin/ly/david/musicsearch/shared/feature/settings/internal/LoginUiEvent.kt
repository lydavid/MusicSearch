package ly.david.musicsearch.shared.feature.settings.internal

import com.slack.circuit.runtime.CircuitUiEvent

internal sealed interface LoginUiEvent : CircuitUiEvent {
    data object StartLogin : LoginUiEvent
    data object DismissDialog : LoginUiEvent
    data class SubmitAuthCode(val authCode: String) : LoginUiEvent
}
