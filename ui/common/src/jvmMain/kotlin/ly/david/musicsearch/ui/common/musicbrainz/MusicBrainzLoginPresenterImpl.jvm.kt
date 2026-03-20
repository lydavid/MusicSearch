package ly.david.musicsearch.ui.common.musicbrainz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.auth.Login
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthorizationUrl
import ly.david.musicsearch.shared.domain.error.HandledException
import kotlin.time.Clock
import kotlin.time.Instant

internal class MusicBrainzLoginPresenterImpl(
    private val login: Login,
    private val musicBrainzAuthorizationUrl: MusicBrainzAuthorizationUrl,
    private val clock: Clock,
) : MusicBrainzLoginPresenter {
    @Composable
    override fun present(): MusicBrainzLoginUiState {
        val uriHandler = LocalUriHandler.current
        var showDialog by rememberSaveable { mutableStateOf(false) }
        var successfulLoginAt: Instant? by rememberSaveable { mutableStateOf(null) }
        var errorMessage: String? by rememberSaveable { mutableStateOf(null) }
        val coroutineScope = rememberCoroutineScope()

        fun eventSink(event: MusicBrainzLoginUiEvent) {
            when (event) {
                MusicBrainzLoginUiEvent.StartLogin -> {
                    uriHandler.openUri(musicBrainzAuthorizationUrl.url)
                    showDialog = true
                }

                MusicBrainzLoginUiEvent.DismissError -> {
                    errorMessage = null
                }

                MusicBrainzLoginUiEvent.DismissDialog -> {
                    showDialog = false
                }

                is MusicBrainzLoginUiEvent.SubmitAuthCode -> {
                    coroutineScope.launch {
                        try {
                            val loginSuccessful = login(event.authCode)
                            if (loginSuccessful) {
                                successfulLoginAt = clock.now()
                            }
                        } catch (ex: HandledException) {
                            errorMessage = ex.userMessage
                        }
                    }
                }
            }
        }

        return MusicBrainzLoginUiState(
            showDialog = showDialog,
            successfulLoginAt = successfulLoginAt,
            errorMessage = errorMessage,
            eventSink = ::eventSink,
        )
    }
}
