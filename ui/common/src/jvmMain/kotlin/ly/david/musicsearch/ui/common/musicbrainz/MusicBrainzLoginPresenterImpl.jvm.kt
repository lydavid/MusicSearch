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

internal class MusicBrainzLoginPresenterImpl(
    private val login: Login,
    private val musicBrainzAuthorizationUrl: MusicBrainzAuthorizationUrl,
) : MusicBrainzLoginPresenter {
    @Composable
    override fun present(): MusicBrainzLoginUiState {
        val uriHandler = LocalUriHandler.current
        var showDialog by rememberSaveable { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        fun eventSink(event: MusicBrainzLoginUiEvent) {
            when (event) {
                MusicBrainzLoginUiEvent.StartLogin -> {
                    uriHandler.openUri(musicBrainzAuthorizationUrl.url)
                    showDialog = true
                }

                MusicBrainzLoginUiEvent.DismissError -> {
                    // no-op
                }

                MusicBrainzLoginUiEvent.DismissDialog -> {
                    showDialog = false
                }

                is MusicBrainzLoginUiEvent.SubmitAuthCode -> {
                    coroutineScope.launch {
                        login(event.authCode)
                    }
                }
            }
        }

        return MusicBrainzLoginUiState(
            showDialog = showDialog,
            eventSink = ::eventSink,
        )
    }
}
