package ly.david.musicsearch.data.musicbrainz

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.musicbrainz.auth.AccessToken
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzOAuthInfo
import ly.david.musicsearch.data.musicbrainz.auth.store.MusicBrainzAuthStore

class Logout(
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzApi: MusicBrainzApi,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val logger: Logger,
) {
    suspend operator fun invoke() {
        val refreshToken = musicBrainzAuthStore.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) return
        try {
            musicBrainzApi.logout(
                token = refreshToken,
                clientId = musicBrainzOAuthInfo.clientId,
                clientSecret = musicBrainzOAuthInfo.clientSecret,
            )
        } catch (ex: Exception) {
            logger.e(ex)
        } finally {
            musicBrainzAuthStore.saveTokens(
                AccessToken(
                    accessToken = "",
                    refreshToken = "",
                ),
            )
            musicBrainzAuthStore.setUsername("")
        }
    }
}
