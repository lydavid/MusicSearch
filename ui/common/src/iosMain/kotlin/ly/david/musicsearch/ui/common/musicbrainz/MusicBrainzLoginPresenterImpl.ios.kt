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
import kotlin.time.Clock
import kotlin.time.Instant

internal class MusicBrainzLoginPresenterImpl(
    private val login: Login,
    private val clock: Clock,
) : MusicBrainzLoginPresenter {
    @Composable
    override fun present(): MusicBrainzLoginUiState {
        val scope = rememberCoroutineScope()
        var successfulLoginAt: Instant? by rememberSaveable { mutableStateOf(null) }
        var errorMessage: String? by rememberSaveable { mutableStateOf(null) }

        fun eventSink(event: MusicBrainzLoginUiEvent) {
            when (event) {
                MusicBrainzLoginUiEvent.StartLogin -> {
                    scope.launch {
                        try {
                            val loginSuccessful = login()
                            if (loginSuccessful) {
                                successfulLoginAt = clock.now()
                            }
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
            successfulLoginAt = successfulLoginAt,
            errorMessage = errorMessage,
            eventSink = ::eventSink,
        )
    }
}
