package ly.david.musicsearch.data.musicbrainz.auth

import kotlinx.coroutines.withContext
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzUserApi
import ly.david.musicsearch.shared.domain.auth.Login
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers

class LoginImpl(
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val musicBrainzUserApi: MusicBrainzUserApi,
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val dispatchers: CoroutineDispatchers,
) : Login {
    override suspend operator fun invoke(authCode: String): Boolean {
        return withContext(dispatchers.io) {
            val response = musicBrainzUserApi.getTokens(
                authCode = authCode,
                musicBrainzOAuthInfo = musicBrainzOAuthInfo,
            )
            musicBrainzAuthStore.saveTokens(
                response.accessToken,
                response.refreshToken,
            )

            val username = musicBrainzUserApi.getUserInfo().username ?: return@withContext false
            musicBrainzAuthStore.setUsername(username)
            true
        }
    }
}
