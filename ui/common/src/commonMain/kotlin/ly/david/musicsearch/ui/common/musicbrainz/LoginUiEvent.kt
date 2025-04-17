package ly.david.musicsearch.ui.common.musicbrainz

import com.slack.circuit.runtime.CircuitUiEvent

sealed interface LoginUiEvent : CircuitUiEvent {
    data object StartLogin : LoginUiEvent
    data object DismissDialog : LoginUiEvent
    data object DismissError : LoginUiEvent
    data class SubmitAuthCode(val authCode: String) : LoginUiEvent
}
