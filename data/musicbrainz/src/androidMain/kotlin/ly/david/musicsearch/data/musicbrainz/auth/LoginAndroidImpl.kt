package ly.david.musicsearch.data.musicbrainz.auth

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import ly.david.musicsearch.shared.domain.auth.LoginAndroid
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.TokenRequest
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoginAndroidImpl(
    private val authService: AuthorizationService,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
    private val logger: Logger,
) : LoginAndroid {
    override suspend operator fun invoke(tokenRequestJsonString: String) {
        val authState: AuthState? = exchangeToken(tokenRequestJsonString)
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

    private suspend fun exchangeToken(jsonRequestString: String): AuthState? {
        return suspendCoroutine { cont ->
            authService.performTokenRequest(
                TokenRequest.jsonDeserialize(jsonRequestString),
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
