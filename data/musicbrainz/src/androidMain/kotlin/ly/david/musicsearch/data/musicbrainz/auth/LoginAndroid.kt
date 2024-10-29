package ly.david.musicsearch.data.musicbrainz.auth

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientSecretBasic
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginAndroid(
    private val authService: AuthorizationService,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
    private val logger: Logger,
) {
    suspend operator fun invoke(result: MusicBrainzLoginActivityResultContract.Result) {
        val exception = result.exception
        val response = result.response
        when {
            exception != null -> {
                logger.e(exception)
            }

            response != null -> {
                val authState = exchangeToken(response)
                musicBrainzAuthStore.saveTokens(
                    accessToken = authState?.accessToken.orEmpty(),
                    refreshToken = authState?.refreshToken.orEmpty(),
                )

                try {
                    val username = musicBrainzUserApi.getUserInfo().username ?: return
                    musicBrainzAuthStore.setUsername(username)
                } catch (ex: Exception) {
                    logger.e(ex)
                }
            }

            else -> {
                logger.e(IllegalStateException("login's result intent is null"))
            }
        }
    }

    private suspend fun exchangeToken(authorizationResponse: AuthorizationResponse): AuthState? {
        return suspendCoroutine { cont ->
            authService.performTokenRequest(
                authorizationResponse.createTokenExchangeRequest(),
                ClientSecretBasic(musicBrainzOAuthInfo.clientSecret),
            ) { response, exception ->
                val authState = AuthState()
                authState.update(
                    response,
                    exception,
                )
                cont.resume(authState)
            }
        }
    }
}
