package ly.david.musicsearch.data.musicbrainz.auth

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.auth.AccessToken
import ly.david.musicsearch.core.models.auth.MusicBrainzAuthStore
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
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
    private val musicBrainzApi: MusicBrainzApi,
    private val logger: Logger,
) {
    suspend operator fun invoke(authorizationResponse: AuthorizationResponse) {
        val authState = exchangeToken(authorizationResponse)
        musicBrainzAuthStore.saveTokens(
            AccessToken(
                accessToken = authState?.accessToken.orEmpty(),
                refreshToken = authState?.refreshToken.orEmpty(),
            ),
        )

        try {
            val username = musicBrainzApi.getUserInfo().username ?: return
            musicBrainzAuthStore.setUsername(username)
        } catch (ex: Exception) {
            logger.e(ex)
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
