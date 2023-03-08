package ly.david.mbjc.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.network.api.MUSIC_BRAINZ_OAUTH_CLIENT_ID
import ly.david.data.network.api.MUSIC_BRAINZ_OAUTH_CLIENT_SECRET
import ly.david.data.network.api.MusicBrainzLogoutApi
import ly.david.data.network.api.MusicBrainzUserApi
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.EndSessionRequest
import net.openid.appauth.EndSessionResponse
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

class MusicBrainzLogoutContract(
    private val authService: AuthorizationService,
    private val serviceConfig: AuthorizationServiceConfiguration,
    private val idToken: String?
) : ActivityResultContract<Unit, MusicBrainzLogoutContract.Result>() {

    data class Result(
        val response: EndSessionResponse?,
        val exception: AuthorizationException?,
    )

    override fun createIntent(context: Context, input: Unit): Intent {

        val endSessionRequest = EndSessionRequest.Builder(serviceConfig)
            .setAdditionalParameters(
                mapOf(
//                    "token" to idToken,
                    "client_id" to MUSIC_BRAINZ_OAUTH_CLIENT_ID,
                    "client_secret" to MUSIC_BRAINZ_OAUTH_CLIENT_SECRET
                )
            )
            .setIdTokenHint(idToken)
            .setPostLogoutRedirectUri(Uri.parse("io.github.lydavid.musicsearch://oauth2/redirect"))
            .build()

        return authService.getEndSessionRequestIntent(endSessionRequest)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result {
        val response = intent?.run { EndSessionResponse.fromIntent(intent) }
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
    private val musicBrainzUserApi: MusicBrainzUserApi,
    private val musicBrainzLogoutApi: MusicBrainzLogoutApi,
    private val authRequest: AuthorizationRequest,
    private val serviceConfig: AuthorizationServiceConfiguration,
    private val authService: AuthorizationService,
    private val clientAuth: ClientAuthentication
) : ViewModel() {

    init {
        // TODO: remove
        viewModelScope.launch {
            try {
                val username = musicBrainzUserApi.getUserInfo().username
                Timber.d("$username")

            } catch (ex: Exception) {
                Timber.d("$ex")
            }
        }
    }

    fun getLoginContract() = MusicBrainzLoginContract(authService, authRequest)

    fun performTokenRequest(authorizationResponse: AuthorizationResponse) {
        authService.performTokenRequest(
            authorizationResponse.createTokenExchangeRequest(),
            clientAuth
        ) { response, exception ->
            viewModelScope.launch {
                runBlocking {
                    val authState = AuthState()
                    authState.update(response, exception)
                    musicBrainzAuthState.setAuthState(authState)

                    // TODO: we get 401 after logging out, then logging back in
                    try {
                        Timber.d("${authState.accessToken}")
                        Timber.d("${response?.accessToken}")
                        val username = musicBrainzUserApi.getUserInfo().username ?: return@runBlocking
                        musicBrainzAuthState.setUsername(username)
                    } catch (ex: Exception) {
                        Timber.d("oof")
                        Timber.d("$ex")
                    }
                }
            }
        }
    }

    fun getLogoutContract() = MusicBrainzLogoutContract(authService, serviceConfig, null)

    fun logout() {
        viewModelScope.launch {
            val authState = musicBrainzAuthState.getAuthState() ?: return@launch
            try {
                musicBrainzLogoutApi.logout(
                    token = authState.refreshToken.orEmpty()
                )
                musicBrainzAuthState.setAuthState(null)
                musicBrainzAuthState.setUsername("")


            } catch (ex: Exception) {
                Timber.d("${ex.message}")

                ex.stackTrace.iterator().forEach {

                    Timber.d("${it}")
                }
                musicBrainzAuthState.setUsername("")
            }
        }
    }

//    fun getLogoutContract() = MusicBrainzLogoutContract(
//        authService,
//        serviceConfig,
//        null//musicBrainzAuthState.getAuthState()?.idToken
//    )

    fun clearToken() {
        // TODO: can't set auth state to null? just set it to empty?
        // TODO: unset username
    }
}
