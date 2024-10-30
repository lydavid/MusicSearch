package ly.david.musicsearch.ui.common.musicbrainz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

// TODO: implement iOS login
internal class LoginPresenterImpl : LoginPresenter {
    @Composable
    override fun present(): LoginUiState {
        var showDialog by rememberSaveable { mutableStateOf(false) }

        fun eventSink(event: LoginUiEvent) {
            when (event) {
                LoginUiEvent.StartLogin -> {
                }

                LoginUiEvent.DismissDialog -> {
                }

                is LoginUiEvent.SubmitAuthCode -> {
                }
            }
        }

        return LoginUiState(
            showDialog = showDialog,
            eventSink = ::eventSink,
        )
    }
}
