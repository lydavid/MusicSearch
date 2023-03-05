package ly.david.mbjc.ui.settings

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.api.MUSIC_BRAINZ_BASE_URL
import ly.david.data.network.api.MUSIC_BRAINZ_OAUTH_CLIENT_ID
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val appPreferences: AppPreferences,
//    private val musicBrainzAuthService: MusicBrainzAuthService
) : ViewModel() {

    fun login(context: Context) {
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/authorize"),  // authorization endpoint
            Uri.parse("$MUSIC_BRAINZ_BASE_URL/oauth2/token") // token endpoint
        )
        val authRequestBuilder = AuthorizationRequest.Builder(
            serviceConfig,
            MUSIC_BRAINZ_OAUTH_CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse("io.github.lydavid.musicsearch://oauth2/redirect")
        )

        val authRequest: AuthorizationRequest = authRequestBuilder
            .setScope("collection profile")
            .build()

        val authService = AuthorizationService(context)

        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
//            .addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP)
//            .setFlags(FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(authIntent)
    }
}
