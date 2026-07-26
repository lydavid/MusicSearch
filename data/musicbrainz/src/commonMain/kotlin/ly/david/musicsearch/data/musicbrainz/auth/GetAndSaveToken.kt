package ly.david.musicsearch.data.musicbrainz.auth

import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore

internal class GetAndSaveToken(
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
) {
    suspend operator fun invoke(
        authorizationCode: String,
        codeVerifier: String? = null,
    ): Boolean {
        val response = musicBrainzUserApi.getTokens(
            authCode = authorizationCode,
            codeVerifier = codeVerifier,
        )
        musicBrainzAuthStore.saveTokens(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            scope = musicBrainzOAuthInfo.scope,
        )

        val username = musicBrainzUserApi.getUserInfo().username ?: return false
        musicBrainzAuthStore.setUsername(username)
        return true
    }
}
