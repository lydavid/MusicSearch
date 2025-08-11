package ly.david.musicsearch.data.musicbrainz.auth

import kotlin.time.Clock
import ly.david.musicsearch.data.musicbrainz.auth.api.MusicBrainzOAuthApi
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore

private const val REFRESH_TOKEN = "refresh_token"

class MusicBrainzAuthRepository(
    private val musicBrainzOAuthInfo: MusicBrainzOAuthInfo,
    private val musicBrainzOAuthApi: MusicBrainzOAuthApi,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
) {

    suspend fun getAccessToken(): String? {
        val accessToken = musicBrainzAuthStore.getAccessToken()
        val refreshToken = musicBrainzAuthStore.getRefreshToken()
        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) return null

        val expiryTime = musicBrainzAuthStore.getExpiryTimeInEpochSeconds()
        val hasExpired = (expiryTime ?: 0L) < Clock.System.now().epochSeconds
        return if (hasExpired) {
            val musicBrainzOAuthResponse = musicBrainzOAuthApi.getAccessToken(
                clientId = musicBrainzOAuthInfo.clientId,
                clientSecret = musicBrainzOAuthInfo.clientSecret,
                grantType = REFRESH_TOKEN,
                refreshToken = refreshToken,
            )
            val newAccessToken = musicBrainzOAuthResponse.accessToken
            val newRefreshToken = musicBrainzOAuthResponse.refreshToken
            musicBrainzAuthStore.saveTokens(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken,
            )
            newAccessToken
        } else {
            accessToken
        }
    }
}
