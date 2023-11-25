package ly.david.musicsearch.data.musicbrainz

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.musicbrainz.auth.AccessToken
import ly.david.musicsearch.data.musicbrainz.auth.store.MusicBrainzAuthStore
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Login(
    private val authService: AuthorizationService,
    private val clientAuth: ClientAuthentication,
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
                clientAuth,
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
