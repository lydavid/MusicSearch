package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import ly.david.musicsearch.data.musicbrainz.auth.LoginJvm
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzAuthorizationUrl

internal class LoginPresenterJvm(
    private val login: LoginJvm,
    private val musicBrainzAuthorizationUrl: MusicBrainzAuthorizationUrl,
) : LoginPresenter {
    @Composable
    override fun present(): LoginUiState {
        val uriHandler = LocalUriHandler.current
        var showDialog by rememberSaveable { mutableStateOf(false) }

        fun eventSink(event: LoginUiEvent) {
            when (event) {
                LoginUiEvent.Login -> {
                    uriHandler.openUri(musicBrainzAuthorizationUrl.url)
                    showDialog = true
                }

                LoginUiEvent.DismissDialog -> {
                    showDialog = false
                }

                is LoginUiEvent.SubmitAuthCode -> {
                    login(event.authCode)
                }
            }
        }

        return LoginUiState(
            showDialog = showDialog,
            eventSink = ::eventSink,
        )
    }
}
