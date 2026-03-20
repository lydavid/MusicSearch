package ly.david.musicsearch.data.musicbrainz.auth

import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import ly.david.musicsearch.shared.domain.auth.Login
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import net.openid.appauth.TokenRequest

class LoginImpl(
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
) : Login {
    override suspend operator fun invoke(tokenRequestJsonString: String): Boolean {
        val tokenRequest: TokenRequest = TokenRequest.jsonDeserialize(tokenRequestJsonString)
        val response = musicBrainzUserApi.getTokens(
            authCode = tokenRequest.authorizationCode.orEmpty(),
            codeVerifier = tokenRequest.codeVerifier,
            musicBrainzOAuthInfo = musicBrainzOAuthInfo,
        )
        musicBrainzAuthStore.saveTokens(
            response.accessToken,
            response.refreshToken,
        )

        val username = musicBrainzUserApi.getUserInfo().username ?: return false
        musicBrainzAuthStore.setUsername(username)
        return true
    }
}
