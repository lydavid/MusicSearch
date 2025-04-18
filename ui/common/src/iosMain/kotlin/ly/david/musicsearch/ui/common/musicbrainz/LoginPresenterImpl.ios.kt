package ly.david.musicsearch.ui.common.musicbrainz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal class LoginPresenterImpl : LoginPresenter {
    @Composable
    override fun present(): LoginUiState {
        var showDialog by rememberSaveable { mutableStateOf(false) }

        fun eventSink(event: LoginUiEvent) {
            when (event) {
                LoginUiEvent.StartLogin -> {
                    // TODO: handle iOS OAuth redirect
                    NSURL.URLWithString(
                        "https://musicbrainz.org/oauth2/authorize" +
                            "?response_type=code" +
                            "&client_id={todo}" +
                            "&redirect_uri=urn%3Aietf%3Awg%3Aoauth%3A2.0%3Aoob" +
                            "&scope=collection%20profile",
                    )?.let { url ->
                        UIApplication.sharedApplication().openURL(url)
                    }
                }

                LoginUiEvent.DismissError -> {
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
