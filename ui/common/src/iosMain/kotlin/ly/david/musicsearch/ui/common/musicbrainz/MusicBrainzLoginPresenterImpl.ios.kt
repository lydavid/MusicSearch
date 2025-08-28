package ly.david.musicsearch.ui.common.musicbrainz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.auth.Login
import ly.david.musicsearch.shared.domain.error.HandledException

internal class MusicBrainzLoginPresenterImpl(
    private val login: Login,
) : MusicBrainzLoginPresenter {
    @Composable
    override fun present(): MusicBrainzLoginUiState {
        val scope = rememberCoroutineScope()
        var errorMessage: String? by rememberSaveable { mutableStateOf(null) }

        fun eventSink(event: MusicBrainzLoginUiEvent) {
            when (event) {
                MusicBrainzLoginUiEvent.StartLogin -> {
                    scope.launch {
                        try {
                            login()
                        } catch (ex: HandledException) {
                            errorMessage = ex.userMessage
                        }
                    }
                }

                MusicBrainzLoginUiEvent.DismissError -> {
                    errorMessage = null
                }

                MusicBrainzLoginUiEvent.DismissDialog -> {
                    // no-op
                }

                is MusicBrainzLoginUiEvent.SubmitAuthCode -> {
                    // no-op
                }
            }
        }

        return MusicBrainzLoginUiState(
            errorMessage = errorMessage,
            eventSink = ::eventSink,
        )
    }
}
