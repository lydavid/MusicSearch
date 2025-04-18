package ly.david.musicsearch.ui.common.musicbrainz

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.auth.LoginAndroid
import ly.david.musicsearch.shared.domain.auth.MusicBrainzLoginActivityResultContract

internal class LoginPresenterImpl(
    private val musicBrainzLoginActivityResultContract: MusicBrainzLoginActivityResultContract,
    private val login: LoginAndroid,
) : LoginPresenter {
    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()
        var errorMessage: String? by rememberSaveable { mutableStateOf(null) }
        val loginLauncher = rememberLauncherForActivityResult(
            contract = musicBrainzLoginActivityResultContract,
        ) { result ->
            when (result) {
                is MusicBrainzLoginActivityResultContract.Result.Success -> {
                    scope.launch {
                        login(result.requestJsonString)
                    }
                }

                is MusicBrainzLoginActivityResultContract.Result.Error -> {
                    errorMessage = result.exceptionString
                }
            }
        }

        fun eventSink(event: LoginUiEvent) {
            when (event) {
                LoginUiEvent.StartLogin -> {
                    loginLauncher.launch(Unit)
                }

                LoginUiEvent.DismissError -> {
                    errorMessage = null
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
            errorMessage = errorMessage,
            eventSink = ::eventSink,
        )
    }
}
