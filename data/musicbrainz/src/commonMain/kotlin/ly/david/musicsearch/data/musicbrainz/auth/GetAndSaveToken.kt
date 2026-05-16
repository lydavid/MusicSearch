package ly.david.musicsearch.data.musicbrainz.auth

import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_OAUTH_SCOPE
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore

internal class GetAndSaveToken(
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
) {
    suspend operator fun invoke(
        authorizationCode: String,
        codeVerifier: String? = null,
    ): Boolean {
        val response = musicBrainzUserApi.getTokens(
            authCode = authorizationCode,
            codeVerifier = codeVerifier,
            musicBrainzOAuthInfo = musicBrainzOAuthInfo,
        )
        musicBrainzAuthStore.saveTokens(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            scope = MUSIC_BRAINZ_OAUTH_SCOPE,
        )

        val username = musicBrainzUserApi.getUserInfo().username ?: return false
        musicBrainzAuthStore.setUsername(username)
        return true
    }
}
