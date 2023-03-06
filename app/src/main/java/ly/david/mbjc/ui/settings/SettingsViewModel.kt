package ly.david.mbjc.ui.settings

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.auth.MusicBrainzAuthState
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication

class MusicBrainzAuthContract(
    private val authRequest: AuthorizationRequest,
    private val authService: AuthorizationService
): ActivityResultContract<Unit, MusicBrainzAuthContract.Result>() {

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
    private val musicBrainzAuthState: MusicBrainzAuthState,
    private val authRequest: AuthorizationRequest,
    private val authService: AuthorizationService,
    private val clientAuth: ClientAuthentication
) : ViewModel() {

    fun getAuthorizationRequest() = MusicBrainzAuthContract(authRequest, authService)

    fun performTokenRequest(authorizationResponse: AuthorizationResponse) {
        authService.performTokenRequest(
            authorizationResponse.createTokenExchangeRequest(),
            clientAuth
        ) { response, exception ->
            val authState = AuthState()
            authState.update(response, exception)
            musicBrainzAuthState.setAuthState(authState)
        }
    }
}
