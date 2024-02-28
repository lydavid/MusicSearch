package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import com.github.scribejava.core.model.OAuth2AccessToken
import com.github.scribejava.core.model.OAuthAsyncRequestCallback
import com.github.scribejava.core.oauth.OAuth20Service
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.auth.AccessToken
import ly.david.musicsearch.data.musicbrainz.auth.LoginJvm

internal class LoginPresenterJvm(
    private val service: OAuth20Service,
    private val login: LoginJvm,
) : LoginPresenter {
    @Composable
    override fun present(): LoginUiState {
        val scope = rememberCoroutineScope()
        val uriHandler = LocalUriHandler.current
        var showDialog by rememberSaveable { mutableStateOf(false) }

        fun eventSink(event: LoginUiEvent) {
            when (event) {
                LoginUiEvent.Login -> {
                    val url = service.authorizationUrl
                    uriHandler.openUri(url)
                    showDialog = true
                }

                LoginUiEvent.DismissDialog -> {
                    showDialog = false
                }

                is LoginUiEvent.SubmitAuthCode -> {
                    service.getAccessToken(
                        event.authCode,
                        object : OAuthAsyncRequestCallback<OAuth2AccessToken> {
                            override fun onCompleted(response: OAuth2AccessToken?) {
                                scope.launch {
                                    login(
                                        AccessToken(
                                            accessToken = response?.accessToken.orEmpty(),
                                            refreshToken = response?.refreshToken.orEmpty(),
                                        ),
                                    )
                                }
                            }

                            override fun onThrowable(t: Throwable?) {
                                println(t)
                            }
                        },
                    )
                }
            }
        }

        return LoginUiState(
            showDialog = showDialog,
            eventSink = ::eventSink,
        )
    }
}
