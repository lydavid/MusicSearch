package ly.david.musicsearch.ui.common.musicbrainz

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.auth.Login
import ly.david.musicsearch.shared.domain.auth.MusicBrainzLoginActivityResultContract

internal class MusicBrainzLoginPresenterImpl(
    private val musicBrainzLoginActivityResultContract: MusicBrainzLoginActivityResultContract,
    private val login: Login,
) : MusicBrainzLoginPresenter {
    @Composable
    override fun present(): MusicBrainzLoginUiState {
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

        fun eventSink(event: MusicBrainzLoginUiEvent) {
            when (event) {
                MusicBrainzLoginUiEvent.StartLogin -> {
                    loginLauncher.launch(Unit)
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
