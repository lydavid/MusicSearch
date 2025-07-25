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

internal class LoginPresenterImpl(
    private val login: Login,
    private val musicBrainzAuthorizationUrl: MusicBrainzAuthorizationUrl,
) : LoginPresenter {
    @Composable
    override fun present(): LoginUiState {
        val uriHandler = LocalUriHandler.current
        var showDialog by rememberSaveable { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        fun eventSink(event: LoginUiEvent) {
            when (event) {
                LoginUiEvent.StartLogin -> {
                    uriHandler.openUri(musicBrainzAuthorizationUrl.url)
                    showDialog = true
                }

                LoginUiEvent.DismissError -> {
                    // no-op
                }

                LoginUiEvent.DismissDialog -> {
                    showDialog = false
                }

                is LoginUiEvent.SubmitAuthCode -> {
                    coroutineScope.launch {
                        login(event.authCode)
                    }
                }
            }
        }

        return LoginUiState(
            showDialog = showDialog,
            eventSink = ::eventSink,
        )
    }
}
