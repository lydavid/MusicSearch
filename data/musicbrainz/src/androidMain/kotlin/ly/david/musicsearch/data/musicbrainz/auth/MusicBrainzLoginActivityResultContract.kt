package ly.david.musicsearch.data.musicbrainz.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.net.toUri
import ly.david.musicsearch.shared.domain.AppInfo
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class MusicBrainzLoginActivityResultContract(
    private val authService: AuthorizationService,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val appInfo: AppInfo,
) : ActivityResultContract<Unit, MusicBrainzLoginActivityResultContract.Result>() {

    data class Result(
        val response: AuthorizationResponse?,
        val exception: AuthorizationException?,
    )

    override fun createIntent(
        context: Context,
        input: Unit,
    ): Intent {
        return authService.getAuthorizationRequestIntent(
            AuthorizationRequest.Builder(
                /* configuration = */
                AuthorizationServiceConfiguration(
                    /* authorizationEndpoint = */
                    musicBrainzOAuthInfo.authorizationEndpoint.toUri(),
                    /* tokenEndpoint = */
                    musicBrainzOAuthInfo.tokenEndpoint.toUri(),
                    /* registrationEndpoint = */
                    null,
                    /* endSessionEndpoint = */
                    musicBrainzOAuthInfo.endSessionEndpoint.toUri(), // Doesn't work cause GET revoke not implemented
                ),
                /* clientId = */
                musicBrainzOAuthInfo.clientId,
                /* responseType = */
                ResponseTypeValues.CODE,
                /* redirectUri = */
                "${appInfo.applicationId}://oauth2/redirect".toUri(),
            )
                .setScope(musicBrainzOAuthInfo.scope)
                .build(),
        )
    }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): Result {
        val response = intent?.run { AuthorizationResponse.fromIntent(intent) }
        val exception = intent?.run { AuthorizationException.fromIntent(intent) }
        return Result(
            response = response,
            exception = exception,
        )
    }
}
