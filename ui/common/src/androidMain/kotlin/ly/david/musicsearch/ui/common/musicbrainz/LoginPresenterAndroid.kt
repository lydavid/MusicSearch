package ly.david.musicsearch.ui.common.musicbrainz

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.data.musicbrainz.auth.LoginAndroid
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzLoginActivityResultContract

internal class LoginPresenterAndroid(
    // TODO: create interface in domain
    private val musicBrainzLoginActivityResultContract: MusicBrainzLoginActivityResultContract,
    private val login: LoginAndroid,
) : LoginPresenter {
    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()
        val loginLauncher = rememberLauncherForActivityResult(contract = musicBrainzLoginActivityResultContract) {
            val result = it
            scope.launch {
                login(result)
            }
        }

        fun eventSink(event: LoginUiEvent) {
            when (event) {
                LoginUiEvent.StartLogin -> {
                    loginLauncher.launch(Unit)
                }

                LoginUiEvent.DismissDialog -> {
                    // no-op
                }

                is LoginUiEvent.SubmitAuthCode -> {
                    // no-op
                }
            }
        }

        return LoginUiState(
            eventSink = ::eventSink,
        )
    }
}
