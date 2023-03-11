package ly.david.mbjc.ui.settings

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.network.api.MusicBrainzAuthApi
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import timber.log.Timber

class MusicBrainzLoginContract(
    private val authService: AuthorizationService,
    private val authRequest: AuthorizationRequest
) : ActivityResultContract<Unit, MusicBrainzLoginContract.Result>() {

    data class Result(
        val response: AuthorizationResponse?,
        val exception: AuthorizationException?,
    )

    override fun createIntent(context: Context, input: Unit): Intent {
        return authService.getAuthorizationRequestIntent(authRequest)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result {
        val response = intent?.run { AuthorizationResponse.fromIntent(intent) }
        val exception = AuthorizationException.fromIntent(intent)
        return Result(
            response = response,
            exception = exception
        )
    }
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val appPreferences: AppPreferences,
    val musicBrainzAuthState: MusicBrainzAuthState,
    private val musicBrainzAuthApi: MusicBrainzAuthApi,
    private val authRequest: AuthorizationRequest,
    private val authService: AuthorizationService,
    private val clientAuth: ClientAuthentication
) : ViewModel() {

    fun getLoginContract() = MusicBrainzLoginContract(authService, authRequest)

    fun performTokenRequest(authorizationResponse: AuthorizationResponse) {
        authService.performTokenRequest(
            authorizationResponse.createTokenExchangeRequest(),
            clientAuth
        ) { response, exception ->
            viewModelScope.launch {
                val authState = AuthState()
                authState.update(response, exception)
                musicBrainzAuthState.setAuthState(authState)

                try {
                    val username = musicBrainzAuthApi.getUserInfo().username ?: return@launch
                    musicBrainzAuthState.setUsername(username)
                } catch (ex: Exception) {
                    // TODO: snackbar
                    Timber.e("$ex")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val authState = musicBrainzAuthState.getAuthState() ?: return@launch
            try {
                musicBrainzAuthApi.logout(
                    token = authState.refreshToken.orEmpty()
                )
            } catch (ex: Exception) {
                // TODO: snackbar
                Timber.e("$ex")
            } finally {
                musicBrainzAuthState.setAuthState(null)
                musicBrainzAuthState.setUsername("")
            }
        }
    }
}
