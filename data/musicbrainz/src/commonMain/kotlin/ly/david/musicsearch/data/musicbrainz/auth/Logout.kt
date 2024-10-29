package ly.david.musicsearch.data.musicbrainz.auth

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore

class Logout(
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val logger: Logger,
) {
    suspend operator fun invoke() {
        val refreshToken = musicBrainzAuthStore.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) return
        try {
            musicBrainzUserApi.logout(
                token = refreshToken,
                clientId = musicBrainzOAuthInfo.clientId,
                clientSecret = musicBrainzOAuthInfo.clientSecret,
            )
        } catch (ex: Exception) {
            logger.e(ex)
        } finally {
            musicBrainzAuthStore.saveTokens(
                accessToken = "",
                refreshToken = "",
            )
            musicBrainzAuthStore.setUsername("")
        }
    }
}
