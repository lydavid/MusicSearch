package ly.david.musicsearch.data.musicbrainz.auth

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.auth.AccessToken
import ly.david.musicsearch.core.models.auth.MusicBrainzAuthStore
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi

class LoginJvm(
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzApi: MusicBrainzApi,
    private val logger: Logger,
) {
    suspend operator fun invoke(accessToken: AccessToken) {
        musicBrainzAuthStore.saveTokens(accessToken)

        try {
            val username = musicBrainzApi.getUserInfo().username ?: return
            musicBrainzAuthStore.setUsername(username)
        } catch (ex: Exception) {
            logger.e(ex)
        }
    }
}
