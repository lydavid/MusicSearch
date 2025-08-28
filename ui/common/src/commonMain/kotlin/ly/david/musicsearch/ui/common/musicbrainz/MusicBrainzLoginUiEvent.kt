package ly.david.musicsearch.ui.common.musicbrainz

import com.slack.circuit.runtime.CircuitUiEvent

sealed interface MusicBrainzLoginUiEvent : CircuitUiEvent {
    data object StartLogin : MusicBrainzLoginUiEvent
    data object DismissDialog : MusicBrainzLoginUiEvent
    data object DismissError : MusicBrainzLoginUiEvent
    data class SubmitAuthCode(val authCode: String) : MusicBrainzLoginUiEvent
}
