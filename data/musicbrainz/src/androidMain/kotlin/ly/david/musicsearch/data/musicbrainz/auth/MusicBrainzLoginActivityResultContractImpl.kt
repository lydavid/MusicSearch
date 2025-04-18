package ly.david.musicsearch.data.musicbrainz.auth

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import ly.david.musicsearch.shared.domain.APPLICATION_ID
import ly.david.musicsearch.shared.domain.auth.MusicBrainzLoginActivityResultContract
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class MusicBrainzLoginActivityResultContractImpl(
    private val authService: AuthorizationService,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
) : MusicBrainzLoginActivityResultContract() {

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
                "$APPLICATION_ID://oauth2/redirect".toUri(),
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
        return if (exception != null) {
            Result.Error(exceptionString = "Failed to get authorization response: ${exception.toJsonString()}")
        } else {
            val tokenExchangeRequestJsonString = response?.createTokenExchangeRequest()?.jsonSerializeString()
            if (tokenExchangeRequestJsonString != null) {
                Result.Success(tokenExchangeRequestJsonString)
            } else {
                Result.Error(exceptionString = "Failed to get token exchange request")
            }
        }
    }
}
