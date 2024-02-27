package ly.david.musicsearch.shared.feature.settings.internal

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.data.musicbrainz.auth.LoginAndroid
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzLoginActivityResultContract

internal class LoginPresenterAndroid(
    private val musicBrainzLoginActivityResultContract: MusicBrainzLoginActivityResultContract,
//    private val logger: Logger,
    private val login: LoginAndroid,
) : LoginPresenter {
    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()
        val loginLauncher = rememberLauncherForActivityResult(contract = musicBrainzLoginActivityResultContract) {
            val result = it
            val exception = result.exception
            val response = result.response
            when {
                exception != null -> {
//                    Timber.e(exception)
                }

                response != null -> {
                    scope.launch {
                        login(response)
                    }
                }

                else -> {
//                    Timber.e("login's result intent is null")
                }
            }
        }

        fun eventSink(event: LoginUiEvent) {
            when (event) {
                LoginUiEvent.Login -> {
                    loginLauncher.launch(Unit)
                }
            }
        }

        return LoginUiState(
            eventSink = ::eventSink,
        )
    }
}
