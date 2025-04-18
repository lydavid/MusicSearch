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

internal class LoginPresenterImpl(
    private val login: Login,
) : LoginPresenter {
    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()
        var errorMessage: String? by rememberSaveable { mutableStateOf(null) }

        fun eventSink(event: LoginUiEvent) {
            when (event) {
                LoginUiEvent.StartLogin -> {
                    scope.launch {
                        try {
                            login()
                        } catch (ex: HandledException) {
                            errorMessage = ex.userMessage
                        }
                    }
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
