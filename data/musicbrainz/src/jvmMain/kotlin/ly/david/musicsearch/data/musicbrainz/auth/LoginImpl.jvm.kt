package ly.david.musicsearch.data.musicbrainz.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import ly.david.musicsearch.shared.domain.auth.Login
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore

class LoginImpl(
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
    private val logger: Logger,
    private val coroutineScope: CoroutineScope,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
) : Login {
    override operator fun invoke(authCode: String) {
        coroutineScope.launch {
            val response = musicBrainzUserApi.getTokens(
                authCode = authCode,
                musicBrainzOAuthInfo = musicBrainzOAuthInfo,
            )
            musicBrainzAuthStore.saveTokens(
                response.accessToken,
                response.refreshToken,
            )

            try {
                val username = musicBrainzUserApi.getUserInfo().username ?: return@launch
                musicBrainzAuthStore.setUsername(username)
            } catch (ex: Exception) {
                logger.e(ex)
            }
        }
    }
}
