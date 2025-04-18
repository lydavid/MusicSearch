package ly.david.musicsearch.ui.common.musicbrainz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthorizationUrl
import ly.david.musicsearch.shared.domain.auth.LoginJvm

internal class LoginPresenterImpl(
    private val login: LoginJvm,
    private val musicBrainzAuthorizationUrl: MusicBrainzAuthorizationUrl,
) : LoginPresenter {
    @Composable
    override fun present(): LoginUiState {
        val uriHandler = LocalUriHandler.current
        var showDialog by rememberSaveable { mutableStateOf(false) }

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
